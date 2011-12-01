package com.chariotsolutions.rabbitmq.web;

import com.chariotsolutions.rabbitmq.domain.Mezzage;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mezzages")
@Controller
@RooWebScaffold(path = "mezzages", formBackingObject = Mezzage.class)
public class MezzageController {
}
