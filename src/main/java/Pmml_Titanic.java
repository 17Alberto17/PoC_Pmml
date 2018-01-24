import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.tree.NodeScoreDistribution;
import org.jpmml.evaluator.tree.TreeModelEvaluator;
import org.jpmml.model.PMMLUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Pmml_Titanic {

    public static void main(String args[]) throws IOException {


        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        int opcion=1, edad = 0, sexo = 0, clase =0, salir=1;


        System.out.println("--------------------------\nPoC PMML en Java [Titanic]\n--------------------------\n");
        String fileNamePmml = "/home/alberto/Escritorio/PMML_J/src/main/resources/Titanic_Model.pmml";


        File pmmlFile = new File(fileNamePmml );
        PMML myPmml = null;

        try {
            myPmml = readPMML(pmmlFile);
        }catch (Exception e){
            e.printStackTrace();
        }


        ModelEvaluator<TreeModel> modelEvaluator = new TreeModelEvaluator(myPmml);
        Map<FieldName, Object> mapa = new HashMap<FieldName, Object>();


        while (opcion==1) {

            System.out.println("Escoga una opción:");
            System.out.println("1.- Jugar\n2.- Salir");

            try {
                opcion = Integer.parseInt(br.readLine());

            } catch (NumberFormatException e) {
                opcion = -99;
            }

            if (opcion == -99) {
                System.out.println("\nPor favor, introduzca un número válido");
                break;
            }

            if (opcion!=1) {
                System.out.println("\n¡Hasta pronto!");
                break;
            }


            while (salir == 1) {

                System.out.println("\nVamos a predecir qué hubiera pasado con un hipotético pasajero del titanic");


                System.out.println("\nIntroduzca el sexo de la persona:\n1.- Femenino\n2.- Masculino ");

                try {
                    sexo = Integer.parseInt(br.readLine());
                    if (sexo < 1 || sexo > 2)
                        sexo = -99;


                } catch (NumberFormatException e) {
                    sexo = -99;
                }

                if (sexo == -99) {
                    System.out.println("\nPor favor, introduzca un número válido\n");
                    break;
                }


                System.out.println("\nIntroduzca la clase en la que viaja la persona:\n1.- 1ra clase\n2.- 2nda clase\n3.- 3ra clase");

                try {
                    clase = Integer.parseInt(br.readLine());
                    if (clase < 1 || clase > 3)
                        clase = -99;

                } catch (NumberFormatException e) {
                    clase = -99;
                }

                if (clase == -99) {
                    System.out.println("\nPor favor, introduzca un número válido\n");
                    break;
                }


                System.out.println("\nIntroduzca la edad de la persona:");

                try {
                    edad = Integer.parseInt(br.readLine());
                    if (edad < 1)
                        edad = -99;

                } catch (NumberFormatException e) {
                    edad = -99;
                }

                if (edad == -99) {
                    System.out.println("\nPor favor, introduzca un número válido\n");
                    break;
                }


                mapa.put(new FieldName("sex"), sexo-1);
                mapa.put(new FieldName("pclass"), clase);
                mapa.put(new FieldName("age"), edad);



                Map<FieldName, ?> resultado = modelEvaluator.evaluate(mapa);
                NodeScoreDistribution aux = (NodeScoreDistribution) resultado.get(new FieldName("survived"));

                String res = "";

                if(aux.getProbability("1")==0.0) {
                    res = (sexo-1)==1 ? "¡Oh! El pasajero ":"¡Oh! La pasajera";
                    res+="de "+edad+" años que viajaba en "+clase+" clase lamentablemente habría fallecido\n";
                }
                else{
                    res = (sexo-1)==1 ? "¡Genial! El pasajero ":"¡Genial! La pasajera";
                    res+="de "+edad+" años que viajaba en "+clase+" clase habría conseguido salvarse\n";
                }

                System.out.println("\n"+res);
                break;
            }
        }
    }

    public static PMML readPMML(File file) throws Exception {

        return PMMLUtil.unmarshal(new FileInputStream(file));

    }
}
