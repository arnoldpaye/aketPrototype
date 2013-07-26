package com.apt.textrank;

import org.apache.commons.math.util.MathUtils;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class MetricVector.java (UTF-8)
 * @date 26/07/2013
 * @author Arnold Paye
 */
public class MetricVector implements Comparable<MetricVector> {

    static Logger log = Logger.getLogger(MetricVector.class);
    private double metric;
    private NodeValue nodeValue;
    private double linkRank;
    private double countRank;
    private double synsetRank;

    public double getMetric() {
        return metric;
    }

    public NodeValue getNodeValue() {
        return nodeValue;
    }

    public MetricVector(NodeValue nodeValue, double linkRank, double countRank, double synsetRank) {
        this.nodeValue = nodeValue;
        this.metric = Math.sqrt(((1.0D * linkRank * linkRank) + (0.5D * countRank * countRank) + (1.5D * synsetRank * synsetRank)) / 3.0D);
        this.linkRank = MathUtils.round(linkRank, 2);
        this.countRank = MathUtils.round(countRank, 2);
        this.synsetRank = MathUtils.round(synsetRank, 2);
        log.debug("mv: " + metric + " " + linkRank + " " + countRank + " " + synsetRank + " " + nodeValue.text);
    }

    @Override
    public int compareTo(MetricVector that) {
        if (this.metric > that.metric) {
            return -1;
        } else if (this.metric < that.metric) {
            return 1;
        } else {
            return this.nodeValue.text.compareTo(that.nodeValue.text);
        }
    }

    public String render() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MathUtils.round(metric, 1));
        stringBuilder.append(' ');
        stringBuilder.append(linkRank);
        stringBuilder.append(' ');
        stringBuilder.append(countRank);
        stringBuilder.append(' ');
        stringBuilder.append(synsetRank);
        return stringBuilder.toString();
    }
}
