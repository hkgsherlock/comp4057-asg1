package org.e5200256.hadoop.bible_most_used_words;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.StringTokenizer;

public class Counter {
    public static class CountMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final IntWritable positive = new IntWritable(1);
        private static final IntWritable negative = new IntWritable(-1);
        private final Text word = new Text();

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString(), "\t"); // tab separated
            if (st.countTokens() == 2) {
                // bible
                st.nextToken(); // omit (1)
                StringTokenizer stWords = new StringTokenizer(st.nextToken()); // space separated
                while (stWords.hasMoreTokens()) {
                    String strWord = stWords.nextToken()
                            .replaceAll("[^A-Za-z']", "")
                            .toLowerCase()
                            .trim();
                    if (strWord.length() == 0) {
                        continue;
                    }
                    word.set(strWord);
                    context.write(word, positive);
                }
            } else if (st.countTokens() == 1) {
                // stopwords
                String strWord = st.nextToken().toLowerCase();
                word.set(strWord);
                context.write(word, negative);
            }
        }
    }

    public static class CountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                if (val.get() < 0) {
                    return;
                }
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
//            context.write(result, key);
        }
    }
}
