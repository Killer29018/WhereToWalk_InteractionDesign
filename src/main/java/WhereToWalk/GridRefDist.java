package WhereToWalk;
// courtesy: https://www.movable-type.co.uk/scripts/os-grid-dist.html

public class GridRefDist {
    public static double gridDistance(String ref1, String ref2) {
        // ref1 & ref2 may be 6- or 8-digit references eg SU387148 or SU38714856

        // convert to fully numeric references
        String[] p1 = gridrefNumeric(ref1);
        String[] p2 = gridrefNumeric(ref2);

        // get E/N distances between ref1 & ref2
        long deltaE = Integer.parseInt(p2[0])-Integer.parseInt(p1[0]);
        long deltaN = Integer.parseInt(p2[1])-Integer.parseInt(p1[1]);

        // and pythagoras gives us the distance between the points
        double dist = Math.sqrt(deltaE*deltaE + deltaN*deltaN);

        return (dist/1000.); // return result in km, 2 decimals
    }

    /*
     * convert standard grid reference ('SU387148') to fully numeric ref ([438700,114800])
     *
     *   note that northern-most grid squares will give 5-digit northings
     *   no error-checking is done on gridref (bad input will give bad results or NaN)
     */
    protected static String[] gridrefNumeric(String gridref) {
        // get numeric values of letter references, mapping A->0, B->1, C->2, etc:
        int letE = gridref.toUpperCase().charAt(0) - (int) 'A';
        int letN = gridref.toUpperCase().charAt(1) - (int) 'A';
        // shuffle down letters after 'I' since 'I' is not used in grid:
        if (letE > 7) letE--;
        if (letN > 7) letN--;
        // convert grid letters into 100km-square indexes from false origin (grid square SV):
        int e = ((letE+3)%5)*5 + (letN%5);
        int n = (int) ((19-Math.floor(letE/5.)*5) - Math.floor(letN/5.));

        // skip grid letters to get numeric part of ref, stripping any spaces:
        gridref = gridref.substring(2).replaceAll("\\s+","");
        // append numeric part of references to grid index:
        String e_str = String.valueOf(e) + gridref.substring(0, gridref.length()/2);
        String n_str = String.valueOf(n) + gridref.substring(gridref.length()/2);

        // normalise to 1m grid:
        switch (gridref.length()) {
            case 6:
                e_str += "00";
                n_str += "00";
                break;
            case 8:
                e_str += "0";
                n_str += "0";
                break;
            // 10-digit refs are already 1m
        }
        return new String[]{e_str, n_str};
    }
}
