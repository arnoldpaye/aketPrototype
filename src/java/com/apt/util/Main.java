package com.apt.util;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

/**
 * @project aketPrototype
 * @package com.apt
 * @class Main.java (UTF-8)
 * @date 28/07/2013
 * @author Arnold Paye
 */
public class Main {
    public static void main(String[] args) {
//        String text = "(esto.),";
//        text = Util.deletePuntuationSigns(text);
//        System.out.println(text);
        SummaryStatistics ss = new SummaryStatistics();
        ss.addValue(7.5);
        ss.addValue(2.5);
        ss.addValue(11.2);
        System.out.println(ss.getMean());
    }
}
