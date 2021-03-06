/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.expath.exist.im4xquery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.Base64BinaryValueType;
import org.exist.xquery.value.BinaryValue;
import org.exist.xquery.value.BinaryValueFromInputStream;
import org.exist.xquery.value.DoubleValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.im4java.core.IM4JavaException;

/**
 *
 * @author zwobit <tobias AT existsolutions.com>
 * @version 1.0
 */

public class RotateFunction extends BasicFunction {
    private static final Logger LOGGER = Logger.getLogger(RotateFunction.class);
    
    public final static FunctionSignature signature = new FunctionSignature(
            new QName("rotate", Im4XQueryModule.NAMESPACE_URI, Im4XQueryModule.PREFIX),
            "Scale the image image to a specified dimension.  If no dimensions are specified, then the default values are 'maxheight = 100' and 'maxwidth = 100'.",
            new SequenceType[]{
                new FunctionParameterSequenceType("image", Type.BASE64_BINARY, Cardinality.EXACTLY_ONE, "The image data"),
                new FunctionParameterSequenceType("degrees", Type.DOUBLE, Cardinality.EXACTLY_ONE, "Degrees by which Image should be rotated."),
                new FunctionParameterSequenceType("mimeType", Type.STRING, Cardinality.EXACTLY_ONE, "The mime-type of the image")
            },
            new FunctionReturnSequenceType(Type.BASE64_BINARY, Cardinality.ZERO_OR_ONE, "the scaled image or an empty sequence if $image is invalid")
    );

    public RotateFunction(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        LOGGER.debug("------------ ScaleFunction ---------------");        
        //was an image and a mime-type speficifed
        if (args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty()) {
            return Sequence.EMPTY_SEQUENCE;
        }

        double degrees = ((DoubleValue) args[1].itemAt(0)).getDouble();
        
        //get the mime-type
        String mimeType = args[2].itemAt(0).getStringValue();
        String formatName = mimeType.substring(mimeType.indexOf("/") + 1);

        //TODO currently ONLY tested for JPEG!!!
        InputStream inputImage = null;
        byte[] outputImage = null;
        
        try {
            

            //get the image data
            inputImage = ((BinaryValue) args[0].itemAt(0)).getInputStream();

            if (inputImage == null) {
                LOGGER.error("Unable to read image data!");
                return Sequence.EMPTY_SEQUENCE;
            }

            //scale the image
            outputImage = Convert.rotate(inputImage, degrees, formatName);

            if (outputImage == null) {
                LOGGER.error("Unable to get output image data!");
                return Sequence.EMPTY_SEQUENCE;
            }
            
            //return the new scaled image data
            return BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(), new ByteArrayInputStream(outputImage));
        } catch (IOException | InterruptedException | IM4JavaException | XPathException e) {
            throw new XPathException(this, e.getMessage());
        }
    }
}
