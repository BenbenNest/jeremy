package com.jeremy.router.core.template;


import com.jeremy.router.annotation.model.RouteMeta;

import java.util.Map;


public interface IRouteGroup {

    void loadInto(Map<String, RouteMeta> atlas);
}
