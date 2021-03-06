package com.jfms.aaa;

import com.jfms.aaa.model.GroupInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(path = "/aaa/group" , produces = "application/json", consumes = "application/json")
public interface GroupApi {
    @RequestMapping(method = RequestMethod.POST)
    String addGroup(@RequestBody GroupInfo groupInfo);

    @RequestMapping(method = RequestMethod.PUT)
    void editGroup(@RequestBody GroupInfo groupInfo) ;

    @RequestMapping(path = "/{gId}", method = RequestMethod.GET)
    GroupInfo getGroup(@PathVariable("gId") String groupId);

    @RequestMapping(path = "/{gId}", method = RequestMethod.DELETE)
    void deleteGroup(@PathVariable("gId") String groupId);
}
