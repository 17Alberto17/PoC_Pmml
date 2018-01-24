import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.tree.NodeScoreDistribution;
import org.jpmml.evaluator.tree.TreeModelEvaluator;
import org.jpmml.model.PMMLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Pmml_Cancer {

    public static void main(String args[]) throws IOException {

        System.out.println("----------------\nPoC PMML en Java\n----------------\n");

        String fileNamePmml = "/home/alberto/Escritorio/PMML_J/src/main/resources/ArbolDecisionCancer.pmml";


        File pmmlFile = new File(fileNamePmml );

        // a PMML object is successfully created from the pmml string:
        PMML myPmml = null;

        try {
            myPmml = readPMML(pmmlFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();

        Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(myPmml);


        ModelEvaluator<TreeModel> modelEvaluator = new TreeModelEvaluator(myPmml);


        Map<FieldName, Object> mapa = new HashMap<FieldName, Object>();

        Double[] datos = {0.99000, 10.38000, 0.80000, 1001.00000, 0.11840, 0.27760, 0.30010, 0.14710, 0.24190, 0.07871};


        mapa.put(new FieldName("x1"), datos[0]);
        mapa.put(new FieldName("x2"), datos[1]);
        mapa.put(new FieldName("x3"), datos[2]);
        mapa.put(new FieldName("x4"), datos[3]);
        mapa.put(new FieldName("x5"), datos[4]);
        mapa.put(new FieldName("x6"), datos[5]);
        mapa.put(new FieldName("x7"), datos[6]);
        mapa.put(new FieldName("x8"), datos[7]);
        mapa.put(new FieldName("x9"), datos[8]);
        mapa.put(new FieldName("x10"),datos[9]);


        evaluator.verify();

        Map<FieldName, ?> resultado = modelEvaluator.evaluate(mapa);

        NodeScoreDistribution aux = (NodeScoreDistribution) resultado.get(new FieldName("y"));


        System.out.println("Diagnostico:");
        System.out.println("------------------------------------------");
        System.out.println("Probabilidad de Cancer Benigno [0]: "+aux.getProbability("0")*100+"%");
        System.out.println("Probabilidad de Cancer Maligno [1]: "+aux.getProbability("1")*100+"%");
        System.out.println("------------------------------------------");


    }

    public static PMML readPMML(File file) throws Exception {

        return PMMLUtil.unmarshal(new FileInputStream(file));

    }
}
