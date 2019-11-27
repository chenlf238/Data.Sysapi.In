package data.wrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @className InputStreamHttpServletRequestWrapper
 * @Description TODO
 * @Author zhaoteng
 * @Date 2019/11/27 9:55
 * @Version 1.0
 **/
public class InputStreamHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public InputStreamHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return super.getInputStream();
    }
}
