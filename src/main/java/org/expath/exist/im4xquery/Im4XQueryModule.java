package org.expath.exist.im4xquery;

import java.util.List;
import java.util.Map;
import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

/**
 *
 * @author zwobit <tobias AT existsolutions.com>
 * @version 1.0
 */
public class Im4XQueryModule extends AbstractInternalModule {

    public final static String NAMESPACE_URI = "http://expath.org/ns/im4xquery";
    public final static String PREFIX = "im4xquery";
    public final static String RELEASED_IN_VERSION = "eXist-2.2";

    private final static FunctionDef[] functions = {
        new FunctionDef(ConvertFunction.signature, ConvertFunction.class),
        new FunctionDef(Convert2PNGFunction.signature, Convert2PNGFunction.class),
        new FunctionDef(Convert2JPGFunction.signature, Convert2JPGFunction.class),
        new FunctionDef(ScaleFunction.signature, ScaleFunction.class),
        new FunctionDef(CropFunction.signature, CropFunction.class),
        new FunctionDef(RotateFunction.signature, RotateFunction.class)
    };

    public Im4XQueryModule(Map<String, List<? extends Object>> parameters) {
        super(functions, parameters);
    }

    @Override
    public String getNamespaceURI() {
        return NAMESPACE_URI;
    }

    @Override
    public String getDefaultPrefix() {
        return PREFIX;
    }

    @Override
    public String getDescription() {
        return "A module for performing operations on Images stored in the eXistdb";
    }

    @Override
    public String getReleaseVersion() {
        return RELEASED_IN_VERSION;
    }

}
