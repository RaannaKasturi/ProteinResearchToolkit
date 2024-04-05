package Backend;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.biojava.nbio.structure.align.StructurePairAligner;
import org.biojava.nbio.structure.align.pairwise.AlternativeAlignment;
import org.biojava.nbio.structure.*;
import org.biojava.nbio.structure.align.ClusterAltAligs;

public class StructAli {
     public static void main(String[] args){

       PDBFileReader pdbr = new PDBFileReader();
       pdbr.setPath("./assets");

       String pdb1 = "1buz";
       String pdb2 = "1ali";
       String outputfile = "./assets/alig_" + pdb1 + "_" + pdb2 + ".pdb";

       try {
           // NO NEED TO DO CHANGE ANYTHING BELOW HERE...

           StructurePairAligner sc = new StructurePairAligner();

           // step1 : read molecules

           System.out.println("aligning " + pdb1 + " vs. " + pdb2);

           Structure s1 = (Structure) pdbr.getStructureById(pdb1);
           Structure s2 = (Structure) pdbr.getStructureById(pdb2);
           // of course you do not have to use the full structures
           // you could also just use any set of atoms of your choice

           // step 2 : do the calculations
           sc.align(s1, s2);

           // if you want more control over the alignment parameters
           // use the StrucAligParameters
           // StrucAligParameters params = new StrucAligParameters();
           // params.setFragmentLength(8);
           // sc.align(s1,s2,params);

           AlternativeAlignment[] aligs = sc.getAlignments();

           // cluster similar results together
           ClusterAltAligs.cluster(aligs);

           // print the result:
           // the AlternativeAlignment object gives access to rotation matrices
           // / shift vectors.
           for (int i = 0; i < aligs.length; i++) {
               AlternativeAlignment aa = aligs[i];
               System.out.println(aa);
           }

           // convert AlternativeAlignment 1 to PDB file, so it can be opened
           // with a viewer of your choice
           // (e.g. Jmol, Rasmol)

           if (aligs.length > 0) {
               AlternativeAlignment aa1 = aligs[0];
               String pdbstr = aa1.toPDB(s1, s2);

               System.out.println("writing alignment to " + outputfile);
               FileOutputStream out = new FileOutputStream(outputfile);
               PrintStream p = new PrintStream(out);

               p.println(pdbstr);

               p.close();
               out.close();
           }
       } catch (FileNotFoundException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (StructureException e) {
           e.printStackTrace();
       }

}
}
