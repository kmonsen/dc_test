package com.kristianm;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
@Controller
public class DeviceContinuityTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceContinuityTestApplication.class, args);
	}

	@GetMapping("/")
    public String main(HttpServletRequest request, Model model) {
        System.out.println(String.format("%15s ", "main") + request.getRequestURL());
		model.addAttribute("greeting", new Greeting());
        return "index";
    }

	@PostMapping("/")
	public String postMain(HttpServletRequest request, @ModelAttribute Greeting greeting, Model model) {
        System.out.println(String.format("%15s ", "postMain") + request.getRequestURL());
	    if (greeting.getName() != null && greeting.getName().trim().length() > 0 &&
	  			greeting.getContent() != null && greeting.getContent().trim().length() > 0) {
			model.addAttribute("greeting", greeting);
		} else {
			model.addAttribute("greeting", new Greeting());
		}

		return "index";
	}

	@GetMapping(path = "/images/{path}")
	public ResponseEntity<byte[]> getImage(HttpServletRequest request, @PathVariable("path") String path) {
    // public ResponseEntity<String> getImage(HttpServletRequest request, @PathVariable("path") String path) {
			System.out.println(String.format("%15s ", "getImage") + request.getRequestURL());
		// return ResponseEntity.ok("" + new java.util.Random().nextInt(10));
		byte[] bytes;
		try {
			var imgFile = new ClassPathResource("images/" + path);
			bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		} catch (IOException e) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity
			.ok()
			.header("Cache-Control", "no-cache, no-store, max-age=0, private")
			.contentType(MediaType.IMAGE_JPEG)
			.body(bytes);
	}

	@GetMapping(path = "/getnum")
	public ResponseEntity<String> getNum(HttpServletRequest request) {
        System.out.println(String.format("%15s ", "getNum") + request.getRequestURL());
		return ResponseEntity.ok("" + new java.util.Random().nextInt(10));
	}

	@RequestMapping("/create_cookie")
	public ResponseEntity createCookie(HttpServletRequest request, HttpServletResponse response, @RequestParam String continue_url) {
        System.out.println(String.format("%15s ", "createCookie") + request.getRequestURL());
		Cookie cookie = new Cookie("dc_cookie", "" + true);
		response.addCookie(cookie);

		return ResponseEntity
			.status(307)
			.header("Location", continue_url)
			.build();
	}

	@RequestMapping("/validate_cookie")
	public ResponseEntity validateCookie(HttpServletRequest request, HttpServletResponse response, @RequestParam String continue_url) {
        System.out.println(String.format("%15s ", "validateCookie") + request.getRequestURL());
		Cookie cookie = new Cookie("dc_cookie", "" + true);
		response.addCookie(cookie);

		return ResponseEntity
			.status(307)
			.header("Location", continue_url)
			.build();
	}
}
