package Parser;

public class FileParser {

    List<DataToInsert> parseData(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // remove date and amount
        lines.remove(0);
        lines.remove(lines.size() - 1);

        return lines.stream()
                .map(s -> s.split("[|]")).map(val -> new DataToInsert(val[0], val[1], val[2])).collect(Collectors.toList());
    }
}
