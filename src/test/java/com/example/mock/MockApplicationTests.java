package com.example.mock;

import com.example.mock.test.MockTestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class MockApplicationTests {

	Logger logger = LogManager.getLogger(MockApplicationTests.class);

	@Autowired
	MockTestController mockTestController;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void createMockController(){
		mockMvc = MockMvcBuilders.standaloneSetup(mockTestController)
				.build();
	}

	@Test
	public void testCallNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/mock/404")
				.accept(MediaType.TEXT_HTML))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testCallBasic() throws Exception {
		String url = "/mock/callBasic";

		//call get
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.accept(MediaType.TEXT_HTML))
				.andExpect(MockMvcResultMatchers.status().isOk());

		//call post
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.accept(MediaType.TEXT_HTML))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testCallCheckReturnStr() throws Exception {
		String param = "TestParam";
		String url = "/mock/callCheckReturnStr";

		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.accept(MediaType.TEXT_HTML)
				.param("data", param))
				.andExpect(MockMvcResultMatchers.content().string(param.concat("_return")))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testCallCheckReturnModel() throws Exception {
		String url = "/mock/callCheckReturnModel";

		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.accept(MediaType.TEXT_HTML))
				.andExpect(MockMvcResultMatchers.model().attribute("key", "value"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testCallWithParam() throws Exception {
		String url = "/mock/callWithParam";
		MultiValueMap<String, String> mv = new LinkedMultiValueMap<>();
		mv.add("data", "String Data");
		mv.add("arrayData", "Array Data1");
		mv.add("arrayData", "Array Data2");

		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.accept(MediaType.TEXT_HTML)
				.params(mv))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testCallWithOneFile() throws Exception{
		String url = "/mock/callWithOneFile";
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Mock Test Text File".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart(url)
				.file(mockMultipartFile))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testCallWithMultiFile() throws Exception{
		String url = "/mock/callWithMultiFile";
		List<MockMultipartFile> mockMultipartFileList = new ArrayList<>();

		for(int i=0; i<5; i++){
			mockMultipartFileList.add(new MockMultipartFile("file", "test_" + i + ".txt", "text/plain", "Mock Test Text File".getBytes()));
		}

		mockMvc.perform(MockMvcRequestBuilders.multipart(url)
				.file(mockMultipartFileList.get(0))
				.file(mockMultipartFileList.get(1))
				.file(mockMultipartFileList.get(2))
				.file(mockMultipartFileList.get(3))
				.file(mockMultipartFileList.get(4)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
}
