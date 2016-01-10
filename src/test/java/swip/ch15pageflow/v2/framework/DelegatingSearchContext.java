package swip.ch15pageflow.v2.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DelegatingSearchContext<T extends SearchContext>  //<1>
    implements SearchContext {
    protected T delegate; // <2>

    public DelegatingSearchContext(T delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return delegate.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return delegate.findElement(by);
    }

}