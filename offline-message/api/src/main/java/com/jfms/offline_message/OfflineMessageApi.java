package com.jfms.offline_message;

import com.jfms.offline_message.model.OfflineMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping(value = "/offline", produces = "application/json", consumes = "application/json")
public interface OfflineMessageApi {
    @RequestMapping(value = "/produce", method = RequestMethod.POST)
    void produceMessage(@RequestBody OfflineMessage offlineMessage);

    @RequestMapping(value = "/consume/{messageOwner}", method = RequestMethod.GET)
    List<String> consumeMessage(@PathVariable("messageOwner") String messageOwner);

}
