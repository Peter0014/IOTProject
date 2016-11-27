package IOT.IOT_SOAP;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;

/**
 * Wrapper class adapter for Hashmap<Long,String>.
 * Code inspired by Apache CFX examples\java_first_jaxws.
 */
public class LongStringHashmapAdapter extends XmlAdapter<LongStringHashmap,HashMap<Long,String>> {

    /**
     * Convert a msTimeString type to a bound type.
     *
     * @param v The msTimeString to be converted. Can be null.
     * @throws Exception if there's an error during the conversion. The caller is responsible for
     *                   reporting the error to the user through {@link ValidationEventHandler}.
     */
    @Override
    public HashMap<Long, String> unmarshal(LongStringHashmap v) throws Exception {
        HashMap<Long,String> map = new HashMap<>();
        for (LongStringHashmap.LongStringEntry e : v.getEntries()) {
            map.put(e.getMsTime(), e.getMsTimeString());
        }
        return map;
    }

    /**
     * Convert a bound type to a msTimeString type.
     *
     * @param v The msTimeString to be convereted. Can be null.
     * @throws Exception if there's an error during the conversion. The caller is responsible for
     *                   reporting the error to the user through {@link ValidationEventHandler}.
     */
    @Override
    public LongStringHashmap marshal(HashMap<Long, String> v) throws Exception {
        LongStringHashmap map = new LongStringHashmap();
        for (HashMap.Entry<Long, String> e : v.entrySet()) {
            LongStringHashmap.LongStringEntry iue = new LongStringHashmap.LongStringEntry();
            iue.setMsTimeString(e.getValue());
            iue.setMsTime(e.getKey());
            map.getEntries().add(iue);
        }
        return map;
    }
}
