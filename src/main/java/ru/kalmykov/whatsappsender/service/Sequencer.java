package ru.kalmykov.whatsappsender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kalmykov.whatsappsender.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class Sequencer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sequencer.class);

    private final List<Message> messages = new ArrayList<>();

    public static <T> List<T> filterOld(List<T> oldMessages, List<T> newMessages) {
        int maxLen = 0;
        int indexOfMaxLen = 0;
        int curLen = 0;
        int curIndex = 0;
        int indexOfmessages = 0;

        while (indexOfmessages < oldMessages.size()) {
            while (indexOfmessages < oldMessages.size() && oldMessages.get(indexOfmessages).equals(newMessages.get(curIndex))) {
                curLen++;
                if (curLen >= maxLen) {
                    maxLen = curLen;
                    indexOfMaxLen = curIndex;
                }
                curIndex++;
                indexOfmessages++;
                if (curIndex==newMessages.size() && indexOfmessages < oldMessages.size()){
                    maxLen = 0;
                    break;
                }
            }
            if (curIndex==0){
                indexOfmessages++;
            }
            curLen = 0;
            curIndex = 0;
        }
        LOGGER.debug(
                "maxLen: {},indexOfMaxLen: {},curLen: {},curIndex: {},indexOfmessages: {}",
                maxLen,
                indexOfMaxLen,
                curLen,
                curIndex,
                indexOfmessages
        );
        return newMessages.subList(maxLen, newMessages.size());
    }
}
