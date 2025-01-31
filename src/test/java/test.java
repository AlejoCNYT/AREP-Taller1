import edu.eci.arep.webserver.HTTPServer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class test {

    @Test
    public void testParseQueryValid() {
        String query = "filename=index.html";
        String result = HTTPServer.parseQuery(query);
        assertEquals("index.html", result, "El nombre del archivo no se extrajo correctamente.");
    }

    @Test
    public void testParseQueryEmpty() {
        String query = "";
        String result = HTTPServer.parseQuery(query);
        assertEquals("", result, "La consulta vacía no se manejó correctamente.");
    }

    @Test
    public void testParseQueryInvalid() {
        String query = "otherparam=value";
        String result = HTTPServer.parseQuery(query);
        assertEquals("", result, "La consulta sin el parámetro 'filename' no se manejó correctamente.");
    }

    @Test
    public void testParsePostDataValid() {
        String postData = "data=example";
        String result = HTTPServer.parsePostData(postData);
        assertEquals("example", result, "Los datos no se extrajeron correctamente.");
    }

    @Test
    public void testParsePostDataEmpty() {
        String postData = "";
        String result = HTTPServer.parsePostData(postData);
        assertEquals("", result, "El cuerpo vacío no se manejó correctamente.");
    }

    @Test
    public void testParsePostDataInvalid() {
        String postData = "otherparam=value";
        String result = HTTPServer.parsePostData(postData);
        assertEquals("", result, "El cuerpo sin el parámetro 'data' no se manejó correctamente.");
    }

    @Test
    public void testProcessData() {
        String data = "example";
        String result = HTTPServer.processData(data);
        assertEquals("EXAMPLE", result, "Los datos no se procesaron correctamente.");
    }

    @Test
    public void testProcessDataEmpty() {
        String data = "";
        String result = HTTPServer.processData(data);
        assertEquals("", result, "Los datos vacíos no se manejaron correctamente.");
    }

    @Test
    public void testGetContentTypeHTML() {
        String filename = "index.html";
        String result = HTTPServer.getContentType(filename);
        assertEquals("text/html", result, "El tipo MIME para HTML no es correcto.");
    }

    @Test
    public void testGetContentTypeCSS() {
        String filename = "style.css";
        String result = HTTPServer.getContentType(filename);
        assertEquals("text/css", result, "El tipo MIME para CSS no es correcto.");
    }

    @Test
    public void testGetContentTypeJS() {
        String filename = "script.js";
        String result = HTTPServer.getContentType(filename);
        assertEquals("application/javascript", result, "El tipo MIME para JavaScript no es correcto.");
    }

    @Test
    public void testGetContentTypePNG() {
        String filename = "image.png";
        String result = HTTPServer.getContentType(filename);
        assertEquals("image/png", result, "El tipo MIME para PNG no es correcto.");
    }

    @Test
    public void testGetContentTypeJPEG() {
        String filename = "photo.jpg";
        String result = HTTPServer.getContentType(filename);
        assertEquals("image/jpeg", result, "El tipo MIME para JPEG no es correcto.");
    }

    @Test
    public void testGetContentTypeUnknown() {
        String filename = "file.unknown";
        String result = HTTPServer.getContentType(filename);
        assertEquals("application/octet-stream", result, "El tipo MIME predeterminado no es correcto.");
    }

}