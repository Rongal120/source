package swip.ch12decorating.baseurl;

import org.apache.commons.lang3.ClassUtils;
import org.openqa.selenium.WebDriver;
import swip.framework.ConfigFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.List;

public class BaseUrlDecorator {

    private static Class[] getInterfaces(Class<?> driverClass) {
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(driverClass);
        return allInterfaces.toArray(new Class[allInterfaces.size()]);
    }

    private static InvocationHandler getInvocationHandler(WebDriver driver) {
        return (proxy, method, args) -> {
            if (method.getName().equals("get")) {
                String url = args[0].toString();
                if (!url.contains("://")) {
                    args[0] = ConfigFactory.BASE_URL + url;
                }
            }

            try {
                return method.invoke(driver, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        };
    }

    public static WebDriver baseUrlDriver(WebDriver driver) {

        Class<?> driverClass = driver.getClass();

        Class[] interfaces = getInterfaces(driverClass);

        InvocationHandler invocationHandler = getInvocationHandler(driver);

        return (WebDriver) Proxy.newProxyInstance(
                driverClass.getClassLoader(),
                interfaces,
                invocationHandler);
    }
}