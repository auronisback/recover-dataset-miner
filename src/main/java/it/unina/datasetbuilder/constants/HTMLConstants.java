package it.unina.datasetbuilder.constants;

public interface HTMLConstants {
    String HTML_START_TAG="<html>";
    String HTML_END_TAG="</table>" +
            "</body>" +
            "</html>";
    String HTML_BODY_OPEN="<body>" +
            "<h2>Data-Builder Results</h2>" +
            "<input type=\"text\" id=\"myInput\" onkeyup=\"myFunction()\" placeholder=\"Search for Status..\" title=\"Type in a status\">" +
            "<table id=\"myTable\">" +
            "<tr>" +
            "<th>Project</th>" +
            "<th>Build</th>" +
            "<th>Job</th>" +
            "<th>Previous Build</th>" +
            "<th>Previous Job</th>" +
            "<th>SHA Commit</th>" +
            "<th>Previous SHA Commit</th>" +
            "<th>Status</th>" +
            "</tr>";
    String HEAD_CONTENT="<style>"+
            "table {"+
            "    font-family: arial, sans-serif;"+
            "   border-collapse: collapse;"+
            "    width: 100%;"+
            "}"+

            "td, th {"+
            "    border: 1px solid #dddddd;"+
            "    text-align: left;"+
            "padding: 8px;"+
            "}"+

            "tr:nth-child(even) {"+
            "    background-color: #dddddd;"+
            "}"+

            "			#myInput {"+
            "    background-position: 10px 10px;"+
            "    background-repeat: no-repeat;"+
            "    width: 100%;"+
            "    font-size: 16px;"+
            "    padding: 12px 20px 12px 40px;"+
            "    border: 1px solid #ddd;"+
            "    margin-bottom: 12px;"+
            "}"+
            "	</style>"+
            "<script>"+
            "function myFunction() {"+
            "    var input, filter, table, tr, td, i, txtValue;"+
            "    input = document.getElementById(\"myInput\");"+
            "            filter = input.value.toUpperCase();"+
            "    table = document.getElementById(\"myTable\");"+
            "            tr = table.getElementsByTagName(\"tr\");"+
            "    for (i = 0; i < tr.length; i++) {"+
            "        td = tr[i].getElementsByTagName(\"td\")[7];"+
            "        if (td) {"+
            "            txtValue = td.textContent || td.innerText;"+
            "            if (txtValue.toUpperCase().indexOf(filter) > -1) {"+
            "                tr[i].style.display = \"\";"+
            "            } else {"+
            "                tr[i].style.display = \"none\";"+
            "            }"+
            "        }"+
            "    }"+
            "}"+
            "	</script>"+
            "</head>";

}
