package IOT.IOT_SOAP;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Wrapper class for HashMap<Long,String>.
 * Code inspired by Apache CFX examples\java_first_jaxws.
 * @author Mai
 * @version M2
 */
@XmlType(name="LongStringHashmap")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="LongStringHashmap")
public class LongStringHashmap {

    @XmlElement(nillable = false, name = "entry")
    ArrayList<LongStringEntry> entries = new ArrayList<>();

    public ArrayList<LongStringEntry> getEntries() {
        return entries;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    static class LongStringEntry {
        @XmlElement(required = true, nillable = false)
        Long msTime;

        String msTimeString;

        public Long getMsTime() {
            return msTime;
        }

        public void setMsTime(Long msTime) {
            this.msTime = msTime;
        }

        public String getMsTimeString() {
            return msTimeString;
        }

        public void setMsTimeString(String msTimeString) {
            this.msTimeString = msTimeString;
        }
    }
}
