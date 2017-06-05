/*
  Licensed under the Apache License, Version 2.0
    http://www.apache.org/licenses/LICENSE-2.0.html

  AUTOGENERATED BY H2O at 2016-11-16T14:22:17.148+05:30
  3.10.0.8
  
  Standalone prediction code with sample test data for NaiveBayesModel named NaiveBayesModel

  How to download, compile and execute:
      mkdir tmpdir
      cd tmpdir
      curl http://127.0.0.1:54321/3/h2o-genmodel.jar > h2o-genmodel.jar
      curl http://127.0.0.1:54321/3/Models.java/NaiveBayesModel > NaiveBayesModel.java
      javac -cp h2o-genmodel.jar -J-Xmx2g -J-XX:MaxPermSize=128m NaiveBayesModel.java

     (Note:  Try java argument -XX:+PrintCompilation to show runtime JIT compiler behavior.)
*/
import java.util.Map;
import hex.genmodel.GenModel;
import hex.genmodel.annotations.ModelPojo;

@ModelPojo(name="NaiveBayesModel", algorithm="naivebayes")
public class NaiveBayesModel extends GenModel {
  public hex.ModelCategory getModelCategory() { return hex.ModelCategory.Multinomial; }
  public boolean isSupervised() { return true; }
  public int nfeatures() { return 4; }
  public int nclasses() { return 8; }

  // Names of columns used by model.
  public static final String[] NAMES = NamesHolder_NaiveBayesModel.VALUES;
  // Number of output classes included in training data response column.
  public static final int NCLASSES = 8;

  // Column domains. The last array contains domain of response column.
  public static final String[][] DOMAINS = new String[][] {
    /* mbvp */ null,
    /* mffdbvp */ null,
    /* mgsr */ null,
    /* mffdgsr */ null,
    /* emotion */ NaiveBayesModel_ColInfo_4.VALUES
  };
  // Prior class distribution
  public static final double[] PRIOR_CLASS_DISTRIB = {0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125};
  // Class distribution used for model building
  public static final double[] MODEL_CLASS_DISTRIB = null;

  public NaiveBayesModel() { super(NAMES,DOMAINS); }
  public String getUUID() { return Long.toString(-2695391419864801696L); }

  // Pass in data in a double[], pre-aligned to the Model's requirements.
  // Jam predictions into the preds[] array; preds[0] is reserved for the
  // main prediction (class for classifiers or value for regression),
  // and remaining columns hold a probability distribution for classifiers.
  public final double[] score0( double[] data, double[] preds ) {
    java.util.Arrays.fill(preds,0);
    double mean, sdev, prob;
    double[] nums = new double[8];
    for(int i = 0; i < 8; i++) {
      nums[i] = Math.log(NaiveBayesModel_APRIORI.VALUES[i]);
      for(int j = 0; j < 0; j++) {
        if(Double.isNaN(data[j])) continue;
        int level = (int)data[j];
        prob = level < 4 ? NaiveBayesModel_PCOND.VALUES[j][i][level] : 0;
        nums[i] += Math.log(prob <= 0.0 ? 0.001 : prob);
      }
      for(int j = 0; j < data.length; j++) {
        if(Double.isNaN(data[j])) continue;
        mean = Double.isNaN(NaiveBayesModel_PCOND.VALUES[j][i][0]) ? 0 : NaiveBayesModel_PCOND.VALUES[j][i][0];
        sdev = Double.isNaN(NaiveBayesModel_PCOND.VALUES[j][i][1]) ? 1 : (NaiveBayesModel_PCOND.VALUES[j][i][1] <= 0.0 ? 0.001 : NaiveBayesModel_PCOND.VALUES[j][i][1]);
        prob = Math.exp(-((data[j]-mean)*(data[j]-mean))/(2.*sdev*sdev)) / (sdev*Math.sqrt(2.*Math.PI));
        nums[i] += Math.log(prob <= 0.0 ? 0.001 : prob);
      }
    }
    double sum;
    for(int i = 0; i < nums.length; i++) {
      sum = 0;
      for(int j = 0; j < nums.length; j++) {
        sum += Math.exp(nums[j]-nums[i]);
      }
      preds[i+1] = 1/sum;
    }
    preds[0] = hex.genmodel.GenModel.getPrediction(preds, PRIOR_CLASS_DISTRIB, data, 0.5);
    return preds;
  }
}
// Count of categorical levels in response.
class NaiveBayesModel_RESCNT implements java.io.Serializable {
  public static final int[] VALUES = new int[8];
  static {
    NaiveBayesModel_RESCNT_0.fill(VALUES);
  }
  static final class NaiveBayesModel_RESCNT_0 implements java.io.Serializable {
    static final void fill(int[] sa) {
      sa[0] = 15;
      sa[1] = 15;
      sa[2] = 15;
      sa[3] = 15;
      sa[4] = 15;
      sa[5] = 15;
      sa[6] = 15;
      sa[7] = 15;
    }
  }
}
// Apriori class distribution of the response.
class NaiveBayesModel_APRIORI implements java.io.Serializable {
  public static final double[] VALUES = new double[8];
  static {
    NaiveBayesModel_APRIORI_0.fill(VALUES);
  }
  static final class NaiveBayesModel_APRIORI_0 implements java.io.Serializable {
    static final void fill(double[] sa) {
      sa[0] = 0.125;
      sa[1] = 0.125;
      sa[2] = 0.125;
      sa[3] = 0.125;
      sa[4] = 0.125;
      sa[5] = 0.125;
      sa[6] = 0.125;
      sa[7] = 0.125;
    }
  }
}
// Conditional probability of predictors.
class NaiveBayesModel_PCOND implements java.io.Serializable {
  public static final double[][][] VALUES = new double[4][][];
  static {
    NaiveBayesModel_PCOND_0.fill(VALUES);
  }
  static class NaiveBayesModel_PCOND_0_0 implements java.io.Serializable {
    public static final double[][] VALUES = new double[8][];
    static {
      NaiveBayesModel_PCOND_0_0_0.fill(VALUES);
    }
    static class NaiveBayesModel_PCOND_0_0_0_0 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_0_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_0_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6705.135321733333;
          sa[1] = 6.651089740478366;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_1 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_1_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_1_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6700.666543866668;
          sa[1] = 6.070570054107519;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_2 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_2_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_2_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6699.177195200001;
          sa[1] = 6.528796350531695;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_3 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_3_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_3_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6696.152656399999;
          sa[1] = 6.035026811242497;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_4 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_4_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_4_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6703.985825333333;
          sa[1] = 18.990497223203082;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_5 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_5_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_5_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6697.516390133332;
          sa[1] = 6.233961088618817;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_6 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_6_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_6_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6694.9114770666665;
          sa[1] = 4.52833970842944;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_0_0_7 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_0_0_7_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_0_0_7_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 6697.959502133334;
          sa[1] = 7.555218890435773;
        }
      }
}
    static final class NaiveBayesModel_PCOND_0_0_0 implements java.io.Serializable {
      static final void fill(double[][] sa) {
        sa[0] = NaiveBayesModel_PCOND_0_0_0_0.VALUES;
        sa[1] = NaiveBayesModel_PCOND_0_0_0_1.VALUES;
        sa[2] = NaiveBayesModel_PCOND_0_0_0_2.VALUES;
        sa[3] = NaiveBayesModel_PCOND_0_0_0_3.VALUES;
        sa[4] = NaiveBayesModel_PCOND_0_0_0_4.VALUES;
        sa[5] = NaiveBayesModel_PCOND_0_0_0_5.VALUES;
        sa[6] = NaiveBayesModel_PCOND_0_0_0_6.VALUES;
        sa[7] = NaiveBayesModel_PCOND_0_0_0_7.VALUES;
      }
    }
}
  static class NaiveBayesModel_PCOND_0_1 implements java.io.Serializable {
    public static final double[][] VALUES = new double[8][];
    static {
      NaiveBayesModel_PCOND_0_1_0.fill(VALUES);
    }
    static class NaiveBayesModel_PCOND_0_1_0_0 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_0_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_0_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.6467554109791922E-4;
          sa[1] = 3.618751157816493E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_1 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_1_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_1_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = -3.0995573376615736E-5;
          sa[1] = 2.713653463519551E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_2 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_2_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_2_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = -2.6226007329920985E-5;
          sa[1] = 2.4617851033959556E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_3 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_3_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_3_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.544493719935411E-5;
          sa[1] = 1.9387428088960427E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_4 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_4_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_4_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = -4.3243185182412305E-5;
          sa[1] = 3.891973670437667E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_5 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_5_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_5_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = -1.0909226267288127E-4;
          sa[1] = 2.4598244774099023E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_6 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_6_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_6_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.7030388712883077E-5;
          sa[1] = 1.267490262427075E-4;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_1_0_7 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_1_0_7_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_1_0_7_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 2.9436545384426925E-5;
          sa[1] = 2.3206239458167354E-4;
        }
      }
}
    static final class NaiveBayesModel_PCOND_0_1_0 implements java.io.Serializable {
      static final void fill(double[][] sa) {
        sa[0] = NaiveBayesModel_PCOND_0_1_0_0.VALUES;
        sa[1] = NaiveBayesModel_PCOND_0_1_0_1.VALUES;
        sa[2] = NaiveBayesModel_PCOND_0_1_0_2.VALUES;
        sa[3] = NaiveBayesModel_PCOND_0_1_0_3.VALUES;
        sa[4] = NaiveBayesModel_PCOND_0_1_0_4.VALUES;
        sa[5] = NaiveBayesModel_PCOND_0_1_0_5.VALUES;
        sa[6] = NaiveBayesModel_PCOND_0_1_0_6.VALUES;
        sa[7] = NaiveBayesModel_PCOND_0_1_0_7.VALUES;
      }
    }
}
  static class NaiveBayesModel_PCOND_0_2 implements java.io.Serializable {
    public static final double[][] VALUES = new double[8][];
    static {
      NaiveBayesModel_PCOND_0_2_0.fill(VALUES);
    }
    static class NaiveBayesModel_PCOND_0_2_0_0 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_0_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_0_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.6472735599673454;
          sa[1] = 0.09198290724703279;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_1 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_1_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_1_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.45116843568119164;
          sa[1] = 0.21121852854766032;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_2 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_2_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_2_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.489340686637029;
          sa[1] = 0.1366714942933929;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_3 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_3_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_3_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.5122982180035426;
          sa[1] = 0.15839686831697308;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_4 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_4_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_4_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.47298326986728534;
          sa[1] = 0.12529512043545682;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_5 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_5_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_5_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.40360342839715035;
          sa[1] = 0.16948923548280945;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_6 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_6_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_6_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.46941784967640116;
          sa[1] = 0.1697789801703828;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_2_0_7 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_2_0_7_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_2_0_7_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 0.43804901072187535;
          sa[1] = 0.1947266990270399;
        }
      }
}
    static final class NaiveBayesModel_PCOND_0_2_0 implements java.io.Serializable {
      static final void fill(double[][] sa) {
        sa[0] = NaiveBayesModel_PCOND_0_2_0_0.VALUES;
        sa[1] = NaiveBayesModel_PCOND_0_2_0_1.VALUES;
        sa[2] = NaiveBayesModel_PCOND_0_2_0_2.VALUES;
        sa[3] = NaiveBayesModel_PCOND_0_2_0_3.VALUES;
        sa[4] = NaiveBayesModel_PCOND_0_2_0_4.VALUES;
        sa[5] = NaiveBayesModel_PCOND_0_2_0_5.VALUES;
        sa[6] = NaiveBayesModel_PCOND_0_2_0_6.VALUES;
        sa[7] = NaiveBayesModel_PCOND_0_2_0_7.VALUES;
      }
    }
}
  static class NaiveBayesModel_PCOND_0_3 implements java.io.Serializable {
    public static final double[][] VALUES = new double[8][];
    static {
      NaiveBayesModel_PCOND_0_3_0.fill(VALUES);
    }
    static class NaiveBayesModel_PCOND_0_3_0_0 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_0_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_0_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.4041779481695773E-9;
          sa[1] = 1.4824410657693907E-8;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_1 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_1_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_1_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 3.37138843894045E-9;
          sa[1] = 8.793951681100824E-9;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_2 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_2_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_2_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.271420311004851E-8;
          sa[1] = 5.869917656693245E-9;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_3 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_3_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_3_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = -1.8843017942132896E-9;
          sa[1] = 1.1052411231489822E-8;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_4 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_4_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_4_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.2689208576895549E-8;
          sa[1] = 1.603571386797814E-8;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_5 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_5_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_5_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = -8.953272883668019E-10;
          sa[1] = 1.0022198038286633E-8;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_6 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_6_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_6_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 1.0169191817651354E-8;
          sa[1] = 1.2369264558332006E-8;
        }
      }
}
    static class NaiveBayesModel_PCOND_0_3_0_7 implements java.io.Serializable {
      public static final double[] VALUES = new double[2];
      static {
        NaiveBayesModel_PCOND_0_3_0_7_0.fill(VALUES);
      }
      static final class NaiveBayesModel_PCOND_0_3_0_7_0 implements java.io.Serializable {
        static final void fill(double[] sa) {
          sa[0] = 2.9556897244469223E-9;
          sa[1] = 6.370429354568311E-9;
        }
      }
}
    static final class NaiveBayesModel_PCOND_0_3_0 implements java.io.Serializable {
      static final void fill(double[][] sa) {
        sa[0] = NaiveBayesModel_PCOND_0_3_0_0.VALUES;
        sa[1] = NaiveBayesModel_PCOND_0_3_0_1.VALUES;
        sa[2] = NaiveBayesModel_PCOND_0_3_0_2.VALUES;
        sa[3] = NaiveBayesModel_PCOND_0_3_0_3.VALUES;
        sa[4] = NaiveBayesModel_PCOND_0_3_0_4.VALUES;
        sa[5] = NaiveBayesModel_PCOND_0_3_0_5.VALUES;
        sa[6] = NaiveBayesModel_PCOND_0_3_0_6.VALUES;
        sa[7] = NaiveBayesModel_PCOND_0_3_0_7.VALUES;
      }
    }
}
  static final class NaiveBayesModel_PCOND_0 implements java.io.Serializable {
    static final void fill(double[][][] sa) {
      sa[0] = NaiveBayesModel_PCOND_0_0.VALUES;
      sa[1] = NaiveBayesModel_PCOND_0_1.VALUES;
      sa[2] = NaiveBayesModel_PCOND_0_2.VALUES;
      sa[3] = NaiveBayesModel_PCOND_0_3.VALUES;
    }
  }
}
// Number of unique levels for each categorical predictor.
class NaiveBayesModel_DOMLEN implements java.io.Serializable {
  public static final double[] VALUES = null;
}
// The class representing training column names
class NamesHolder_NaiveBayesModel implements java.io.Serializable {
  public static final String[] VALUES = new String[4];
  static {
    NamesHolder_NaiveBayesModel_0.fill(VALUES);
  }
  static final class NamesHolder_NaiveBayesModel_0 implements java.io.Serializable {
    static final void fill(String[] sa) {
      sa[0] = "mbvp";
      sa[1] = "mffdbvp";
      sa[2] = "mgsr";
      sa[3] = "mffdgsr";
    }
  }
}
// The class representing column emotion
class NaiveBayesModel_ColInfo_4 implements java.io.Serializable {
  public static final String[] VALUES = new String[8];
  static {
    NaiveBayesModel_ColInfo_4_0.fill(VALUES);
  }
  static final class NaiveBayesModel_ColInfo_4_0 implements java.io.Serializable {
    static final void fill(String[] sa) {
      sa[0] = "anger";
      sa[1] = "grief";
      sa[2] = "hate";
      sa[3] = "joy";
      sa[4] = "no emotion";
      sa[5] = "platonic";
      sa[6] = "reverence";
      sa[7] = "romantic";
    }
  }
}

