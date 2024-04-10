package Backend;

import Frontend.MainFrame;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.gui.BiojavaJmol;
import org.biojava.nbio.structure.io.PDBFileReader;

public class VisStruct {
    private MainFrame mainFrame;

    public VisStruct(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void VisStruct(String PDBID) {
        try {
            PDBFileReader pdbr = new PDBFileReader();
            System.setProperty("PDB_DIR", "./assets");
            String pdbCode = PDBID;
            Structure struc = pdbr.getStructureById(pdbCode);
            BiojavaJmol jmolPanel = new BiojavaJmol();
            jmolPanel.setStructure(struc);
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
