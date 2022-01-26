package it.unina.recoverminer.utilities;

import com.hp.gagawa.java.elements.*;
import org.junit.Test;

public class GagawaTest {
    @Test
    public void gagaTest(){
        Html html= new Html();
        Head h= new Head().appendText(
                "\t\t<style>\n" +
                        "\t\t\t\ttable {\n" +
                        "\t\t\t\t  font-family: arial, sans-serif;\n" +
                        "\t\t\t\t  border-collapse: collapse;\n" +
                        "\t\t\t\t  width: 100%;\n" +
                        "\t\t\t\t}\n" +
                        "\n" +
                        "\t\t\t\ttd, th {\n" +
                        "\t\t\t\t  border: 1px solid #dddddd;\n" +
                        "\t\t\t\t  text-align: left;\n" +
                        "\t\t\t\t  padding: 8px;\n" +
                        "\t\t\t\t}\n" +
                        "\n" +
                        "\t\t\t\ttr:nth-child(even) {\n" +
                        "\t\t\t\t  background-color: #dddddd;\n" +
                        "\t\t\t\t}\n" +
                        "\n" +
                        "\t\t\t\t#myInput {\n" +
                        "\t\t\t\t  background-position: 10px 10px;\n" +
                        "\t\t\t\t  background-repeat: no-repeat;\n" +
                        "\t\t\t\t  width: 100%;\n" +
                        "\t\t\t\t  font-size: 16px;\n" +
                        "\t\t\t\t  padding: 12px 20px 12px 40px;\n" +
                        "\t\t\t\t  border: 1px solid #ddd;\n" +
                        "\t\t\t\t  margin-bottom: 12px;\n" +
                        "\t\t\t\t}\n" +
                        "\t\t</style>\t\t\t\n" +
                        "\t\t<script>\n" +
                        "\t\t\t\tfunction myFunction() {\n" +
                        "\t\t\t\t  var input, filter, table, tr, td, i, txtValue;\n" +
                        "\t\t\t\t  input = document.getElementById(\"myInput\");\n" +
                        "\t\t\t\t  filter = input.value.toUpperCase();\n" +
                        "\t\t\t\t  table = document.getElementById(\"myTable\");\n" +
                        "\t\t\t\t  tr = table.getElementsByTagName(\"tr\");\n" +
                        "\t\t\t\t  for (i = 0; i < tr.length; i++) {\n" +
                        "\t\t\t\t\ttd = tr[i].getElementsByTagName(\"td\")[7];\n" +
                        "\t\t\t\t\tif (td) {\n" +
                        "\t\t\t\t\t  txtValue = td.textContent || td.innerText;\n" +
                        "\t\t\t\t\t  if (txtValue.toUpperCase().indexOf(filter) > -1) {\n" +
                        "\t\t\t\t\t\ttr[i].style.display = \"\";\n" +
                        "\t\t\t\t\t  } else {\n" +
                        "\t\t\t\t\t\ttr[i].style.display = \"none\";\n" +
                        "\t\t\t\t\t  }\n" +
                        "\t\t\t\t\t}       \n" +
                        "\t\t\t\t  }\n" +
                        "\t\t\t\t}\n" +
                        "\t\t</script>\n");
        Body b= new Body();
        Table t = new Table();
        b.appendChild(t);
        Tr row = new Tr();
        t.appendChild(row);
        Td cell= new Td();
        cell.appendText("Sono una cella");
        row.appendChild(cell);
        html.appendChild(h,b);
        System.out.println(html.write());
    }
}
