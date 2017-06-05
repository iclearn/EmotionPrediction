# EmotionPrediction
Emotion State Prediction Using Physiological Signals in Real Time

featureExtraction.R extracts features from the raw data (obtained from MIT Media Labs) and creates classification models using the <b>H2O AI platform</b> with the following algorithms.

Model          - File

<b>Deep learning</b>  - DeepLearningModel.java

<b>Gradient Boosting Machine</b> - GBMModel.java

<b>Naive Bayes Classification</b> - NaiveBayesModel.java


EmotionPredictionDataSpout.java contains the spout class for the topologies created in Apache Storm

EmotionPredictionTopology.java is the topology using the GBMModel for classification.

EmotionPredictionTopologyDL.java is the topology using the Deep Learning for classification.

Index.html is the html page that offers fun visualization of the emotional state in real time.

Change the paths in the files to point to your data or output files.

View <b>project.mp4</b> to see the project running.
