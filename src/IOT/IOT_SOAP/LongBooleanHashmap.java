package IOT.IOT_SOAP;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Wrapper class for HashMap<Long,Boolean>.
 * Code inspired by Apache CFX examples\java_first_jaxws.
 * @author Mai
 * @version M2
 */
@XmlType(name= "LongBooleanHashmap")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="LongBooleanHashmap")
public class LongBooleanHashmap {

    @XmlElement(nillable = false, name = "entry")
    ArrayList<LongBooleanEntry> entries = new ArrayList<>();

    public ArrayList<LongBooleanEntry> getEntries() {
        return entries;
    }

    /**
     * Hashmap wrapper helper class. Is annoted as XmlRootElement and has the XmlAccressType FIELD.
     */
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    static class LongBooleanEntry {
        @XmlElement(required = true, nillable = false)
        Long msTime;

        Boolean msTimeBool;

        public Long getMsTime() {
            return msTime;
        }

        public void setMsTime(Long msTime) {
            this.msTime = msTime;
        }

        public Boolean getMsTimeBool() {
            return msTimeBool;
        }

        public void setMsTimeBool(Boolean msTimeBool) {
            this.msTimeBool = msTimeBool;
        }
    }
}
