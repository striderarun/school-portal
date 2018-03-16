package com.school.rest;

import com.box.sdk.BoxAPIConnection;
import com.school.beans.ApplicationUser;
import com.school.beans.StudentBean;
import com.school.box.BoxHelper;
import com.school.logging.Loggable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/services/enterprise")
public class EnterpriseRestService {

    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> validateEnterpriseConnection() {
        BoxAPIConnection adminClient = BoxHelper.adminClient();
        if (adminClient != null) {
            return Collections.singletonMap("status", true);
        } else {
            return Collections.singletonMap("status", false);
        }
    }

    @Loggable
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> login(@RequestBody ApplicationUser applicationUser) {
        String boxId = BoxHelper.boxIdFromRequest(applicationUser.getUserName());
        if (StringUtils.isNotBlank(boxId)) {
            return Collections.singletonMap("boxId", boxId);
        } else {
            return Collections.singletonMap("boxId", "");
        }
    }
}
