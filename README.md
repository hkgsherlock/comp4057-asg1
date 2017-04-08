# COMP4057 Programming Assignment 1 - Bible Word Count
*by Poon Chun Yiu (`15200256`)*

9 April 2017

## Objectives
To find the most frequent words (but not stopwords) in bible.

## Development and Compile Requirement
* Software side
    * [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) >= 1.8(.0.25)
    * [Maven](https://maven.apache.org/download.cgi) >= 3.3.3
* Maven side
    * `org.apache.hadoop:hadoop-common` >= 2.7.3
    * `org.apache.hadoop:hadoop-mapreduce-client-core` >= 2.7.3

## Make

### Build
1. `mvn clean`
2. `mvn package -Dmaven.test.skip=true`
3. The newest `BibleMostUsedWords.jar` will be exist on `target/`.

### Run
#### Local

```
scp target/BibleMostUsedWords.jar etc/bible.txt etc/stopwords.txt {SID}@faith.comp.hkbu.edu.hk:~
ssh {SID}@faith.comp.hkbu.edu.hk
```

#### `faith.comp.hkbu.edu.hk`

```
cd ~
scp BibleMostUsedWords.jar bible.txt stopwords.txt csr42:~
ssh csr42
```

#### `csr42`

```
cd ~
rm -rf ./src-asg1
mkdir ./src-asg1
mv BibleMostUsedWords.jar bible.txt stopwords.txt ./src-asg1/
cd ./src-asg1
jar xvf BibleMostUsedWords.jar
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:$(pwd)/*
hadoop fs -rm -r -f ~/input_txt_bible ~/temp_txt_bible ~/output_txt_bible
hadoop fs -mkdir input_txt_bible
hadoop fs -put bible.txt stopwords.txt ~/input_txt_bible/
hadoop jar BibleMostUsedWords.jar org/e5200256/hadoop/bible_most_used_words/Driver ~/input_txt_bible ~/temp_txt_bible ~/output_txt_bible
hadoop fs -cat ~/output_txt_bible/*

# optional
hadoop fs -rm -r -f ~/input_txt_bible ~/temp_txt_bible ~/output_txt_bible

# remember to `exit` to prevent from being banned, if you know what I mean
exit
```

#### `faith.comp.hkbu.edu.hk`

```
exit
```
