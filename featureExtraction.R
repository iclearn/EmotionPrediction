## feature extraction  -- heart rate and gsr
## inverse of interbeat signals detected from raw bvp signal - heart rate signal

# to work with excel sheets in R
if('xlsx' %in% installed.packages() == F){
  install.packages('xlsx')
}

# signal processing - hanning function
if('signal' %in% installed.packages() == F){
  install.packages('signal')
}

# to work with data.table - faster than data.frame
if('data.table' %in% installed.packages() == F){
  install.packages('data.table')
}

# to perform lda/qda
if('MASS' %in% installed.packages() == F){
  install.packages('MASS')
}

if('h2o' %in% installed.packages() == F){
  install.packages('h2o')
}

# --------------------------------------------------------------- #
library(xlsx)
library(signal)
library(data.table)
library(MASS)
library(h2o)
# --------------------------------------------------------------- #

# data frame to store four features extracted and the emotion they represent
# mean bvp, mean first forward diff bvp, mean normalised gsr, mean normalised ffd of gsr, emotion
n <- 10^6
emdata <- data.table("mbvp" = rep(0, n), "mffdbvp" = rep(0, n), "mgsr" = rep(0, n), "mffdgsr" = rep(0, n), 
                     "emotion" = rep(factor(c('noemotion', 'anger', 'hate', 'grief', 'plove', 'rlove', 'joy', 'reverence')), n))
rr = 1L
assign('emdata', emdata, envir = .GlobalEnv)
assign('rr', rr, envir = .GlobalEnv)

# --------------------------------------------------------------- #

safeSystem <- function(x) {
  print(sprintf("+ CMD: %s", x))
  res <- system(x)
  print(res)
  if (res != 0) {
    msg <- sprintf("SYSTEM COMMAND FAILED (exit status %d)", res)
    stop(msg)
  }
}

# to generate the filenames of data
num <- c(1:20)
for(i in num){
  fname <- paste('day', i, '.xlsx', sep = "")
  data <- read.xlsx(fname, 1, header = FALSE, colClasses = "double")
  colnames(data) <- c('emg_noemotion', 'emg_anger', 'emg_hate', 'emg_grief', 'emg_plove', 'emg_rlove', 'emg_joy', 'emg_reverence',
                      'bvp_noemotion', 'bvp_anger', 'bvp_hate', 'bvp_grief', 'bvp_plove', 'bvp_rlove', 'bvp_joy', 'bvp_reverence',
                      'gsr_noemotion', 'gsr_anger', 'gsr_hate', 'gsr_grief', 'gsr_plove', 'gsr_rlove', 'gsr_joy', 'gsr_reverence',
                      'resp_noemotion', 'resp_anger', 'resp_hate', 'resp_grief', 'resp_plove', 'resp_rlove', 'resp_joy', 'resp_reverence')
  
  # ------------------------------------------------------------ #
  # data frame to hold feature values for all emotions
  res <- matrix(nrow = 4, ncol = 8)
  res <- as.data.frame(res, row.names = c('mub', 'delb', 'nmug', 'ndelg'))
  colnames(res) <- c('noemotion', 'anger', 'hate', 'grief', 'plove', 'rlove', 'joy', 'reverence')
  
  # data frame to store smoothed data
  # nrows = length(data) + 500 - 1 if convolution is done with a 500 point hanning frame
  sdata <- as.data.frame(matrix(ncol = 16, nrow = 2500))
  
  
  # ------------------------------------------------------------ #
  
  # smoothing
  # convolution of raw signal with hanning 500 point values
  
  # bvp signal
  j <- c(9:16)
  for(jj in j){
    # column 1-8 carries bvp data in sdata
    sdata[,jj-8] <- conv(data[,jj], hanning(500))
  }
  
  # gsr signal
  j <- c(17:24)
  for(jj in j){
    # column 9-16 carries gsr data in sdata
    sdata[,jj-8] <- conv(data[,jj], hanning(500))
  }
  
  # ------------------------------------------------------------- #
  # feature extraction
  
  # bvp signal
  j <- c(1:8)
  # mean
  for(jj in j){
    res[1, jj] <- mean(sdata[, jj])
  }
  # mean first forward difference
  for(jj in j){
    res[2, jj] <- mean(sdata[, jj][c(2, 2500)] - sdata[, jj][c(1, 2499)])
  }
  
  # -------------------------------------------------------------- #
  # gsr signal - calculation of min and max over entire day session for normalization
  mi <- 10000
  ma <- -10000
  for(j in c(9:16)){
    mi <- min(min(sdata[, j]), mi)
    ma <- max(max(sdata[, j]), ma)
  }
  
  # normalization
  j <- c(9:16)
  for(jj in j){
    sdata[, jj] <- (sdata[, jj] - mi) / (ma - mi)
  }
  
  # feature calculations
  # mean
  for(jj in j){
    res[3, jj-8] <- mean(sdata[, jj])
  }
  
  # mean first forward difference
  for(jj in j){
    res[4, jj-8] <- mean(sdata[, jj][c(2, 2500)] - sdata[, jj][c(1, 2499)])
  }
  # write it in a data structure - array of these structures
  emotion <- factor(c('no emotion', 'anger', 'hate', 'grief', 'platonic', 'romantic', 'joy', 'reverence'))
  for(j in c(1:8)){
    set(emdata, rr, 1L, res[,j][1])
    set(emdata, rr, 2L, res[,j][2])
    set(emdata, rr, 3L, res[,j][3])
    set(emdata, rr, 4L, res[,j][4])
    set(emdata, rr, 5L, emotion[j])
    rr <- rr + 1L
    # emdata <<- rbind.data.frame(emdata, data.frame(, res[,i][2],res[,i][3],res[,i][4], emotion[i]))
  }
}

# deletes extra rows
emdata <- emdata[(mbvp != 0)]

# ------------------------------------------------------------------------- #
## model formation and testing

# sample data for learning (75%) and testing (25%)
learn <- emdata[emdata[, .I[sample(.N, 0.75*nrow(emdata)/8)], by = emotion]$V1]
test <- emdata[-emdata[, .I[sample(.N, 0.75*nrow(emdata)/8)], by = emotion]$V1]

#test$emotion <- NULL
# write test to a file testdata.csv
#write.csv('testdata.csv', x = test, row.names = F)

# ------------------------------------------------------------------------- #
# lda model learning
#emlda <- lda(emotion ~ mbvp + mffdbvp + mgsr, data = emdata)

# prediction by the model ... testing
#predict(emlda, learn)$class

# or 
#summary(predict(emlda, learn)$class)

## may also have a look at
#predict(emlda, learn)$posterior

# ------------------------------------------------------------------------ #

# starting h2o 
myIP <- 'localhost'
myPort <- 54321

h2o.init(ip = myIP, port = myPort, startH2O = T, nthreads = -1, max_mem_size = '2G')

learn.h2o <- as.h2o(learn)
test.h2o <- as.h2o(test)

label <- c('emotion')
predictors <- c('mbvp', 'mffdbvp', 'mgsr', 'mffdgsr')

# ------------------------------------------------------------------------ #

# gbm - gradient boosting machine - 0.67

gbm.model <- h2o.gbm(training_frame = learn.h2o, x = 1:4, y = 5, model_id = 'GBMModel', ntrees = 10)
# cmd <- sprintf('mkdir %s', 'generated_model')
# safeSystem(cmd)
h2o.download_pojo(gbm.model, './generated_model/')

predictions <- h2o.predict(object = gbm.model, newdata = test.h2o)
accuracy <- mean(predictions$predict == test.h2o$emotion)
print(paste("accuracy of the gbm model is:", accuracy))

# ------------------------------------------------------------------------ #

# naive bayes classification model 0.3
navbs.model <- h2o.naiveBayes(training_frame = learn.h2o, x = 1:4, y = 5, model_id = 'NaiveBayesModel')
h2o.download_pojo(navbs.model, './generated_model/')

predictions <- h2o.predict(object = navbs.model, newdata = test.h2o)
accuracy <- mean(predictions$predict == test.h2o$emotion)
print(paste("accuracy of the naive bayes model is:", accuracy))

# ----------------------------------------------------------------------- #

# deep learning classification model - 0.85
deeplearn.model <- h2o.deeplearning(training_frame = learn.h2o, x = 1:4, y = 5, model_id = 'DeepLearningModel1', nfolds = 15, 
                                    activation = 'Tanh', epochs = 1e3)
h2o.download_pojo(deeplearn.model, './generated_model/')

predictions <- h2o.predict(object = deeplearn.model, newdata = test.h2o)
accuracy <- mean(predictions$predict == test.h2o$emotion)
print(paste("accuracy of the deep learning model is:", accuracy))

# ---------------------------------------------------------------------- #

# deep learning 25 folds - with dropout 0.275
# deep learning classification model
#deeplearn.model <- h2o.deeplearning(training_frame = learn.h2o, x = 1:4, y = 5, model_id = 'DeepLearningModel1', nfolds = 25, 
#activation = 'TanhWithDropout', epochs = 1e3, input_dropout_ratio = 0.1)
#h2o.download_pojo(deeplearn.model, './generated_model/')

#predictions <- h2o.predict(object = deeplearn.model, newdata = test.h2o)
#accuracy <- mean(predictions$predict == test.h2o$emotion)
#print(paste("accuracy of the deep learning model is:", accuracy))

# -------------------------------------------------------------------- #

h2o.shutdown(prompt = F)  