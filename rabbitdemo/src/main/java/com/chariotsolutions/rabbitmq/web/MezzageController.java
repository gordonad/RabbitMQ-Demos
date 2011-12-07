package com.chariotsolutions.rabbitmq.web;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.chariotsolutions.rabbitmq.domain.Mezzage;
import com.chariotsolutions.rabbitmq.service.MezzageService;

@RequestMapping("/mezzages")
@Controller
public class MezzageController {
    private static final Logger logger = LoggerFactory.getLogger(MezzageController.class);

	@Autowired
	MezzageService mezzageService;

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Mezzage mezzage,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("mezzage", mezzage);
			return "mezzages/create";
		}
		uiModel.asMap().clear();
		mezzageService.saveMezzage(mezzage);
		return "redirect:/mezzages/"
				+ encodeUrlPathSegment(mezzage.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("mezzage", new Mezzage());
		return "mezzages/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id,
			Model uiModel) {
		uiModel.addAttribute("mezzage", mezzageService.findMezzage(id));
		uiModel.addAttribute("itemId", id);
		return "mezzages/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size;
			final int firstResult = page == null ? 0 : (page - 1)
					* sizeNo;
			uiModel.addAttribute("mezzages",
					mezzageService.findMezzageEntries(firstResult, sizeNo));
			float nrOfPages = (float) mezzageService.countAllMezzages()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("mezzages", mezzageService.findAllMezzages());
		}
		return "mezzages/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid Mezzage mezzage,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("mezzage", mezzage);
			return "mezzages/update";
		}
		uiModel.asMap().clear();
		mezzageService.updateMezzage(mezzage);
		return "redirect:/mezzages/"
				+ encodeUrlPathSegment(mezzage.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id,
			Model uiModel) {
		uiModel.addAttribute("mezzage", mezzageService.findMezzage(id));
		return "mezzages/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(
			@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Mezzage mezzage = mezzageService.findMezzage(id);
		mezzageService.deleteMezzage(mezzage);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/mezzages";
	}

	@ModelAttribute("mezzages")
	public Collection<Mezzage> populateMezzages() {
		return mezzageService.findAllMezzages();
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
