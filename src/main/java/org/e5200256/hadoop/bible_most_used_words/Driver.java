package org.e5200256.hadoop.bible_most_used_words;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Driver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        String[] rArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

        if (rArgs.length != 3) {
            System.err.println("Usage: BibleMostUsedWords <in> <temp> <out>");
            System.exit(2);
        }

        Job job1 = Job.getInstance(conf, "BibleMostUsedWords: Counter");

        FileInputFormat.addInputPath(job1, new Path(rArgs[0]));
        FileOutputFormat.setOutputPath(job1, new Path(rArgs[1]));

        job1.setJarByClass(Counter.class);

        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(IntWritable.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);

        job1.setMapperClass(Counter.CountMapper.class);
        job1.setReducerClass(Counter.CountReducer.class);

        if (!job1.waitForCompletion(true)) {
            System.exit(1);
            return;
        }

        Job job2 = Job.getInstance(conf, "BibleMostUsedWords: First10");

        FileInputFormat.addInputPath(job2, new Path(rArgs[1]));
        FileOutputFormat.setOutputPath(job2, new Path(rArgs[2]));

        job2.setJarByClass(SwappedHead.class);

        job2.setNumReduceTasks(1);

        job2.setMapOutputKeyClass(IntWritable.class);
        job2.setMapOutputValueClass(Text.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);

        job2.setSortComparatorClass(SwappedHead.SHReverseComparator.class);

        job2.setMapperClass(SwappedHead.SHMapper.class);
        job2.setReducerClass(SwappedHead.SHReducer.class);

        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}
