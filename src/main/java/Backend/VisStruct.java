package Backend;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.gui.BiojavaJmol;
import org.biojava.nbio.structure.io.PDBFileReader;

public class VisStruct {
    
    public void VisStruct(String PDBID) {
        try {
            PDBFileReader pdbr = new PDBFileReader();
            pdbr.setPath("./assets");
            String pdbCode = PDBID;
            Structure struc = pdbr.getStructureById(pdbCode);
            BiojavaJmol jmolPanel = new BiojavaJmol();
            jmolPanel.setStructure(struc);
            // send some RASMOL style commands to Jmol
            jmolPanel.evalString("select * ; backbone on;");
            jmolPanel.evalString("select * ; color chain;");
            jmolPanel.evalString("select *; spacefill off; wireframe off; backbone 0.4;  ");
            jmolPanel.evalString("save STATE state_1");
            jmolPanel.evalString("spin on;");
        } catch (Exception e){
            e.printStackTrace();
	}
    }
}
