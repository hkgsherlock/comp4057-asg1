package org.e5200256.hadoop.bible_most_used_words;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.StringTokenizer;

public class SwappedHead {
    public static class SHMapper extends Mapper<Object, Text, IntWritable, Text> {
        private final Text word = new Text();
        private final IntWritable number = new IntWritable();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString());
            word.set(st.nextToken());
            number.set(Integer.valueOf(st.nextToken()));
            context.write(number, word);
        }
    }

    public static class SHReverseComparator extends IntWritable.Comparator {
        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            return -1 * super.compare(b1, s1, l1, b2, s2, l2);
        }
    }

    public static class SHReducer extends Reducer<IntWritable, Text, Text, IntWritable> {
        private int i = 0;
        @Override
        protected void reduce(IntWritable value, Iterable<Text> keys, Context context) throws IOException, InterruptedException {
            if (i >= 10) {
                return;
            }
            for (Text k: keys) {
                if (i++ >= 10) {
                    break;
                }
                context.write(k, value);
            }
        }
    }
}
