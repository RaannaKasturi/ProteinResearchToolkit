package Backend;

import Frontend.MainFrame;
import java.io.IOException;
import java.util.*;
import org.biojava.nbio.structure.*;
import org.biojava.nbio.structure.align.StructureAlignment;
import org.biojava.nbio.structure.align.StructureAlignmentFactory;
import org.biojava.nbio.structure.align.ce.CeMain;
import org.biojava.nbio.structure.align.gui.MultipleAlignmentJmolDisplay;
import org.biojava.nbio.structure.align.multiple.MultipleAlignment;
import org.biojava.nbio.structure.align.multiple.mc.MultipleMcMain;
import org.biojava.nbio.structure.align.multiple.util.MultipleAlignmentWriter;
import org.biojava.nbio.structure.align.util.AtomCache;

public class StructAli {
    private MainFrame mainFrame;
    public StructAli(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public void AlignStruct(String[] PDBIs) {
        System.setProperty("PDB_DIR", "./assets");
        try {
            List<String> names = Arrays.asList("3app", "4ape", "5pep", "1psn", "4cms", "1bbs.A", "1smr.A");

            //Load the CA atoms of the structures and create the structure identifiers
            AtomCache cache = new AtomCache();
            List<Atom[]> atomArrays = new ArrayList<Atom[]>();
            List<StructureIdentifier> identifiers = new ArrayList<StructureIdentifier>();
            for (String name:names)	{
              atomArrays.add(cache.getAtoms(name));
              identifiers.add(new SubstructureIdentifier(name));
            }

            //Generate the multiple alignment algorithm with the chosen pairwise algorithm
            StructureAlignment pairwise  = StructureAlignmentFactory.getAlgorithm(CeMain.algorithmName);
            MultipleMcMain multiple = new MultipleMcMain(pairwise);

            //Perform the alignment
            MultipleAlignment result = multiple.align(atomArrays);

            // Set the structure identifiers, so that each atom array can be identified in the outputs
            result.getEnsemble().setStructureIdentifiers(identifiers);

            //Output the FASTA sequence alignment
            System.out.println(MultipleAlignmentWriter.toFASTA(result));

            //Display the results in a 3D view
            MultipleAlignmentJmolDisplay.display(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}