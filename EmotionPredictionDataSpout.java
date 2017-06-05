package org.apache.storm.starter;

import org.apache.storm.Config;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ssquasar on 13/11/16.
 */
public class EmotionPredictionDataSpout extends BaseRichSpout {

    public static Logger LOG = LoggerFactory.getLogger(EmotionPredictionDataSpout.class);
    boolean _isDistributed;
    SpoutOutputCollector _collector;
    AtomicInteger _cnt = new AtomicInteger(0);


    public EmotionPredictionDataSpout() {
        this(true);
    }

    public EmotionPredictionDataSpout(boolean isDistributed) {
        _isDistributed = isDistributed;
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
    }

    public void close() {

    }

    public void nextTuple() {
        Utils.sleep(1000);
        File file = new File("/home/ssquasar/Desktop/iot/DataSet/testdata.csv");  // EDIT ME TO YOUR PATH!
        String[] observation=null;
        int i = 0;
        try {
            String line="";
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (i++<=_cnt.get()) line = br.readLine(); // stream through to next line
            if(line!=null)
            observation = line.split(",");
        } catch (Exception e) {
            e.printStackTrace();
            _cnt.set(0);
        }
        _cnt.getAndIncrement();
        if (_cnt.get() == 1000) _cnt.set(0); // force reset, for demo only!!!
        if(observation!=null)
        _collector.emit(new Values(observation));
    }

    public void ack(Object msgId) {
        //empty
    }

    public void fail(Object msgId) {
        //empty
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        LinkedList<String> fields_list = new LinkedList<String>(Arrays.asList(GBMModel.NAMES));

        
        String[] fields = fields_list.toArray(new String[fields_list.size()]); // emit these fields
        declarer.declare(new Fields(fields));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        if(!_isDistributed) {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
            return ret;
        } else {
            return null;
        }
    }
}
