package org.apache.storm.starter;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ssquasar on 13/11/16.
 */
public class EmotionPredictionTopology {

    public static class PredictionBolt extends BaseRichBolt {
        OutputCollector _collector;

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {

            GBMModel p = new GBMModel();

            // get the input tuple as a String[]
            ArrayList<String> vals_string = new ArrayList<String>();
            for (Object v : tuple.getValues()) vals_string.add((String)v);
            String[] raw_data = vals_string.toArray(new String[vals_string.size()]);

            // the score pojo requires a single double[] of input.
            // We handle all of the categorical mapping ourselves
            double data[] = new double[raw_data.length];  // leave out the extra field added

            String[] colnames = tuple.getFields().toList().toArray(new String[tuple.size()]);

            // if the column is a factor column, then look up the value, otherwise put the double
            for (int i = 0; i < raw_data.length; ++i) {
                data[i] = Double.valueOf(raw_data[i]);
            }

            // get the predictions
            double[] preds = new double [GBMModel.NCLASSES+1];
            //p.predict(data, preds);
            p.score0(data, preds);

            // emit the results
            // emotion class having max probability is emitted
            int max = 1;
            for(int i = 1; i < preds.length; ++i){
                if(preds[i] > preds[max]){
                    max = i;
                }
            }
            String[] emotions = {"No Emotion", "Anger", "Hate", "Grief", "Platonic Love",
                    "Romantic Love", "Joy", "Reverence"};
            _collector.emit(tuple, new Values(max, emotions[max-1]));
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("index", "emotion"));
        }


    }

    /**
     * The ClassifierBolt receives the input probabilities and then makes a classification.
     * It uses a threshold value to determine how to classify the observation, which is computed based on the validation
     * done during model fitting.
     */
    public static class ClassifierBolt extends BaseRichBolt {
        OutputCollector _collector;

        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {

            String expected = tuple.getString(1);
            String content = "The current emotion of the user is: " + expected;
            System.out.println(content);
            try {
                File file = new File("/home/ssquasar/Desktop/iot/DataSet/out"); // EDIT ME TO YOUR PATH!
                if (!file.exists())  file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(expected);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            _collector.emit(tuple, new Values(expected));
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("emotion"));
        }
    }

    @Test
    public static void emotion_prediction() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("input_row", new EmotionPredictionDataSpout(), 10);
        builder.setBolt("score_probabilities", new PredictionBolt(), 3).shuffleGrouping("input_row");
        builder.setBolt("classify", new ClassifierBolt(), 3).shuffleGrouping("score_probabilities");

        Config conf = new Config();
        conf.setDebug(true);

        String[] args = null;
        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        }
        else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(1000 * 60 * 60); // run for 1 hour
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}
