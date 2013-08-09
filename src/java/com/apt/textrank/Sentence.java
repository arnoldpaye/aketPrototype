package com.apt.textrank;

import java.util.List;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Sentence.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class Sentence {

    static Logger log = Logger.getLogger(Sentence.class);
    private String text;
    private String[] tokenList;
    private Node[] nodeList;

    public String[] getTokenList() {
        return tokenList;
    }

    public String getText() {
        return text;
    }

    public Sentence(String text) {
        this.text = text;
    }

    public Node[] getNodeList() {
        return nodeList;
    }

    public void mapTokens(Language language, Graph graph, List<String> postFilterList) {
        tokenList = language.tokenizeSentence(text);
        String[] tagList = language.tagTokens(tokenList);
//        log.debug(text);
//        for (int i = 0; i < tokenList.length; i++) {
//            log.debug(tokenList[i] + " " + tagList[i]);
//        }
//        System.out.println("DBG Sentence: " + text);
//        System.out.println("DBG length: " + tokenList.length);
//        for (int i = 0; i < tokenList.length; i++) {
//            System.out.println("DBG " + i + ": " + tokenList[i] + " " + tagList[i]);
//        }
        int numberOfTokens = tokenList.length;
        Node lastNode = null;
        nodeList = new Node[numberOfTokens];
        for (int i = 0; i < numberOfTokens; i++) {
            String pos = tagList[i];
            String token = tokenList[i];
            log.debug("token: " + tokenList[i] + " pos tag: " + pos);
            if (language.isRelevant(pos, postFilterList)) {
//                System.out.println("DBG is relevant: " + i);
                log.debug("isRelevant: " + token + "->" + pos);
                String key = language.getNodeKey(token, pos);
                KeyWord keyWord = new KeyWord(token, pos);
                Node node = Node.buildNode(graph, key, keyWord);
                if (lastNode != null) {
                    node.connect(lastNode);
                }
                lastNode = node;
                nodeList[i] = node;
//                System.out.println("DBG " + i + ": " + node.getNodeValueText());
//                for (Node n : node.getEdges()) {
//                    System.out.println("DBG\t\t" + n.getNodeValueText());
//                }
            }
        }
//        for (int i = 0; i < nodeList.length; i++) {
//            if (nodeList[i] != null) {
//                System.out.println("DBG " + i + ": " + nodeList[i].getNodeValueText());
//                for (Node node : nodeList[i].getEdges()) {
//                    System.out.println("DBG\t\t" + node.getNodeValueText());
//                }
//            }
//        }
    }
}
