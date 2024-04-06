import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.PairwiseAlignment;
import org.biojava.nbio.alignment.template.PairwiseAlignmentResult;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.PDBFile;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.align.fatcat.FatCatFlexible;
import org.biojava.nbio.structure.align.fatcat.FatCatFlexibleResult;
import org.jmol.api.JmolViewer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class test {

    public static void main(String[] args) throws IOException {

        // Retrieve protein structures from PDB
        Structure structure1 = PDBFile.read(new File("1AKJ.pdb")).getStructure("1AKJ");
        Structure structure2 = PDBFile.read(new File("1BXL.pdb")).getStructure("1BXL");

        // Perform multiple structure alignment using FatCatFlexible algorithm
        FatCatFlexible fatCatFlexible = new FatCatFlexible(1.0, 1.0, 1.0);
        FatCatFlexibleResult result = fatCatFlexible.align(structure1.getPolymerSequence(), structure2.getPolymerSequence());

        // Visualize the aligned protein structure using Jmol
        JmolViewer jmolViewer = new JmolViewer();
        jmolViewer.setJmolStatusListener(System.out::println);
        jmolViewer.display("jmol");

        // Set background color
        jmolViewer.eval("color background white");

        // Set the protein structures to be displayed
        jmolViewer.append("load " + structure1.getPDBCode() + ".pdb" + " chain A");
        jmolViewer.append("load " + structure2.getPDBCode() + ".pdb" + " chain A");

        // Apply the alignment transformation to the second protein structure
        List<Atom> alignedAtoms = result.getAlignedAtoms(1, 2);
        if (!alignedAtoms.isEmpty()) {
            Atom firstAtom = alignedAtoms.get(0);
            Atom lastAtom = alignedAtoms.get(alignedAtoms.size() - 1);
            double[] transformation = Alignments.getAlignmentTransform(firstAtom.getCoordinate(),
                    lastAtom.getCoordinate(), true);
            jmolViewer.eval("transform " + transformation[0] + " " + transformation[1] + " " + transformation[2] + " " +
                    transformation[3] + " " + transformation[4] + " " + transformation[5] + " " +
                    "chain A of " + structure2.getPDBCode() + ".pdb");
        }

        // Color the aligned residues
        for (Group group1 : structure1.getModel().getChains().get(0).getGroups()) {
            if (group1 instanceof Atom) {
                Atom atom1 = (Atom) group1;
                for (Group group2 : structure2.getModel().getChains().get(0).getGroups()) {
                    if (group2 instanceof Atom && result.getAlignment().getMatch(atom1.getSeqNum(), group2.getSeqNum())) {
                        Atom atom2 = (Atom) group2;
                        jmolViewer.eval("color red " + atom1.getPDBName() + " of chain A of " + structure1.getPDBCode() + ".pdb");
                        jmolViewer.eval("color red " + atom2.getPDBName() + " of chain A of " + structure2.getPDBCode() + ".pdb");
                    }
                }
            }
        }

        // Display the aligned protein structures
        jmolViewer.eval("zoom fit");
        jmolViewer.eval("spacefill 50 off");
        jmolViewer.eval("cartoons on");
        jmolViewer.eval("wireframe on");

        // Wait for user input
        jmolViewer.script("wait for mouseclick");

        // Close the Jmol viewer
        jmolViewer.script("exit");
    }
}