import play.mvc.EssentialFilter;
import play.filters.cors.CORSFilter;
import play.http.HttpFilters;

import javax.inject.Inject;

public class Filters implements HttpFilters {

    @Inject
    private CORSFilter corsFilter;

    public final EssentialFilter[] filters() {
        return new EssentialFilter[] {corsFilter.asJava()};
    }
}
