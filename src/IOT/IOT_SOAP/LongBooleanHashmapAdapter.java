package IOT.IOT_SOAP;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;

/**
 * Wrapper class adapter for Hashmap<Long,String>.
 * Code inspired by Apache CFX examples\java_first_jaxws.
 */
public class LongBooleanHashmapAdapter extends XmlAdapter<LongBooleanHashmap,HashMap<Long,Boolean>> {

    /**
     * Convert a msTimeString type to a bound type.
     *
     * @param v The msTimeString to be converted. Can be null.
     * @throws Exception if there's an error during the conversion. The caller is responsible for
     *                   reporting the error to the user through {@link ValidationEventHandler}.
     */
    @Override
    public HashMap<Long, Boolean> unmarshal(LongBooleanHashmap v) throws Exception {
        HashMap<Long,Boolean> map = new HashMap<>();
        for (LongBooleanHashmap.LongBooleanEntry e : v.getEntries()) {
            map.put(e.getMsTime(), e.getMsTimeBool());
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
    public LongBooleanHashmap marshal(HashMap<Long, Boolean> v) throws Exception {
        LongBooleanHashmap map = new LongBooleanHashmap();
        for (HashMap.Entry<Long, Boolean> e : v.entrySet()) {
            LongBooleanHashmap.LongBooleanEntry iue = new LongBooleanHashmap.LongBooleanEntry();
            iue.setMsTimeBool(e.getValue());
            iue.setMsTime(e.getKey());
            map.getEntries().add(iue);
        }
        return map;
    }
}
