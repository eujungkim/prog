package krog.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//@WebServlet(name = "UploadFile", urlPatterns = {"/UploadFile"},
//        initParams = { @WebInitParam(name = "uploadpath", value = "/var/www/upload/") })
@MultipartConfig
public class UploadFile extends HttpServlet {

	private static final long serialVersionUID = 4392311475844219655L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/plain;charset=UTF-8");

        ServletOutputStream os = response.getOutputStream();

        Part filePart = request.getPart("icon");
        String fileName = filePart.getSubmittedFileName();
        InputStream is = filePart.getInputStream();
        Files.copy(is, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        
        Part fieldPart = request.getPart("fruit");
        InputStreamReader inputStreamReader = new InputStreamReader(fieldPart.getInputStream());
        Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
        String streamToString = streamOfString.collect(Collectors.joining());
        System.out.println(streamToString);

        os.print("File successfully uploaded");
    }
}
