package nadav.tasher.lightool;

import android.app.Activity;
import android.widget.TextView;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Stringer {
    public static String reversed(String s) {
        String news = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            news = news + s.charAt(i);
        }
        return news;
    }

    public static class Encryptor {
        public static String CONTROL = "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\n\b\t\u000B\f\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F ";
        public static String LATIN = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F ¡¢£¤¥¦§¨©ª«¬\u00AD®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƀƁƂƃƄƅƆƇƈƉƊƋƌƍƎƏƐƑƒƓƔƕƖƗƘƙƚƛƜƝƞƟƠơƢƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷƸƹƺƻƼƽƾƿǀǁǂǃǄǅǆǇǈǉǊǋǌǍǎǏǐǑǒǓǔǕǖǗǘǙǚǛǜǝǞǟǠǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰǱǲǳǴǵǶǷǸǹǺǻǼǽǾǿȀȁȂȃȄȅȆȇȈȉȊȋȌȍȎȏȐȑȒȓȔȕȖȗȘșȚțȜȝȞȟȠȡȢȣȤȥȦȧȨȩȪȫȬȭȮȯȰȱȲȳȴȵȶȷȸȹȺȻȼȽȾȿɀɁɂɃɄɅɆɇɈɉɊɋɌɍɎɏ";
        public static String GREEK = "ͰͱͲͳʹ͵Ͷͷ\u0378\u0379ͺͻͼͽ;\u037F\u0380\u0381\u0382\u0383΄΅Ά·ΈΉΊ\u038BΌ\u038DΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡ\u03A2ΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώϏϐϑϒϓϔϕϖϗϘϙϚϛϜϝϞϟϠϡϢϣϤϥϦϧϨϩϪϫϬϭϮϯϰϱϲϳϴϵ϶ϷϸϹϺϻϼϽϾϿ";
        public static String HEBREW = "\u0590ְֱֲֳִֵֶַָֹֺֻּֽ֑֖֛֢֣֤֥֦֧֪֚֭֮֒֓֔֕֗֘֙֜֝֞֟֠֡֨֩֫֬֯־ֿ׀ׁׂ׃ׅׄ׆ׇ\u05C8\u05C9\u05CA\u05CB\u05CC\u05CD\u05CE\u05CFאבגדהוזחטיךכלםמןנסעףפץצקרשת\u05EB\u05EC\u05ED\u05EE\u05EFװױײ׳״\u05F5\u05F6\u05F7\u05F8\u05F9\u05FA\u05FB\u05FC\u05FD\u05FE";
        public static String LATINEXTENTION = "ḀḁḂḃḄḅḆḇḈḉḊḋḌḍḎḏḐḑḒḓḔḕḖḗḘḙḚḛḜḝḞḟḠḡḢḣḤḥḦḧḨḩḪḫḬḭḮḯḰḱḲḳḴḵḶḷḸḹḺḻḼḽḾḿṀṁṂṃṄṅṆṇṈṉṊṋṌṍṎṏṐṑṒṓṔṕṖṗṘṙṚṛṜṝṞṟṠṡṢṣṤṥṦṧṨṩṪṫṬṭṮṯṰṱṲṳṴṵṶṷṸṹṺṻṼṽṾṿẀẁẂẃẄẅẆẇẈẉẊẋẌẍẎẏẐẑẒẓẔẕẖẗẘẙẚẛẜẝẞẟẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹỺỻỼỽỾỿ";
        public static String SYMBOLIC = "‐‑‒–—―‖‗‘’‚‛“”„‟†‡•‣․‥…‧\u2028\u2029\u202A\u202B\u202C\u202D\u202E‰‱′″‴‵‶‷‸‹›※‼‽‾‿⁀⁁⁂⁃⁄⁅⁆⁇⁈⁉⁊⁋⁌⁍⁎⁏⁐⁑⁒⁓⁔⁕⁖⁗⁘⁙⁚⁛⁜⁝⁞\u2060\u2061\u2062\u2063\u2064\u2065\u2066\u2067\u2068\u2069\u206A\u206B\u206C\u206D\u206E\u206F";
        public static String SUPERANDSUB = "⁰ⁱ\u2072\u2073⁴⁵⁶⁷⁸⁹⁺⁻⁼⁽⁾ⁿ₀₁₂₃₄₅₆₇₈₉₊₋₌₍₎\u208Fₐₑₒₓₔₕₖₗₘₙₚₛₜ\u209D\u209E\u209F";
        public static String CURRENCIES = "₠₡₢₣₤₥₦₧₨₩₪₫€₭₮₯₰₱₲₳₴₵₶₷₸₹₺\u20BB\u20BC\u20BD\u20BE\u20BF\u20C0\u20C1\u20C2\u20C3\u20C4\u20C5\u20C6\u20C7\u20C8\u20C9\u20CA\u20CB\u20CC\u20CD\u20CE\u20CF";
        public static String LETTERLIKE = "℀℁ℂ℃℄℅℆ℇ℈℉ℊℋℌℍℎℏℐℑℒℓ℔ℕ№℗℘ℙℚℛℜℝ℞℟℠℡™℣ℤ℥Ω℧ℨ℩KÅℬℭ℮ℯℰℱℲℳℴℵℶℷℸℹ℺℻ℼℽℾℿ⅀⅁⅂⅃⅄ⅅⅆⅇⅈⅉ⅊⅋⅌⅍ⅎ⅏";
        public static String ALL = CONTROL + LATIN + GREEK + HEBREW + LATINEXTENTION + SYMBOLIC + SUPERANDSUB + CURRENCIES + LETTERLIKE;
        private static String DEFAULT = ALL;

        public static class EncryptionV1 {
            public static String encrypt(String key, String text) {
                if (key != null && key.length() > 0) {
                    int keyPart = 0;
                    String encrypted = "";
                    for (int l = 0; l < text.length(); l++) {
                        int keyChar = findChar(key.charAt(keyPart), DEFAULT);
                        int currentChar = findChar(text.charAt(l), DEFAULT);
                        if (currentChar == -1) {
                            encrypted = encrypted + text.charAt(l);
                        } else {
                            if (currentChar + keyChar >= DEFAULT.length()) {
                                encrypted = encrypted + DEFAULT.charAt(currentChar + keyChar - DEFAULT.length());
                            } else {
                                encrypted = encrypted + DEFAULT.charAt(currentChar + keyChar);
                            }
                        }
                        if (keyPart == key.length() - 1) {
                            keyPart = 0;
                        } else {
                            keyPart++;
                        }
                    }
                    return encrypted;
                }
                return text;
            }

            public static String decrypt(String key, String text) {
                if (key != null && key.length() > 0) {
                    int keyPart = 0;
                    String encrypted = "";
                    for (int l = 0; l < text.length(); l++) {
                        int keyChar = findChar(key.charAt(keyPart), DEFAULT);
                        int currentChar = findChar(text.charAt(l), DEFAULT);
                        if (currentChar == -1) {
                            encrypted = encrypted + text.charAt(l);
                        } else {
                            if (currentChar - keyChar < 0) {
                                encrypted = encrypted + DEFAULT.charAt(currentChar - keyChar + DEFAULT.length());
                            } else {
                                encrypted = encrypted + DEFAULT.charAt(currentChar - keyChar);
                            }
                        }
                        if (keyPart == key.length() - 1) {
                            keyPart = 0;
                        } else {
                            keyPart++;
                        }
                    }
                    return encrypted;
                }
                return text;
            }

            public static String encrypt(String key, String text, String mask) {
                if (key != null && key.length() > 0) {
                    int keyPart = 0;
                    String encrypted = "";
                    for (int l = 0; l < text.length(); l++) {
                        int keyChar = findChar(key.charAt(keyPart), mask);
                        int currentChar = findChar(text.charAt(l), mask);
                        if (currentChar == -1) {
                            encrypted = encrypted + text.charAt(l);
                        } else {
                            if (currentChar + keyChar >= mask.length()) {
                                encrypted = encrypted + mask.charAt(currentChar + keyChar - mask.length());
                            } else {
                                encrypted = encrypted + mask.charAt(currentChar + keyChar);
                            }
                        }
                        if (keyPart == key.length() - 1) {
                            keyPart = 0;
                        } else {
                            keyPart++;
                        }
                    }
                    return encrypted;
                }
                return text;
            }

            public static String decrypt(String key, String text, String mask) {
                if (key != null && key.length() > 0) {
                    int keyPart = 0;
                    String encrypted = "";
                    for (int l = 0; l < text.length(); l++) {
                        int keyChar = findChar(key.charAt(keyPart), mask);
                        int currentChar = findChar(text.charAt(l), mask);
                        if (currentChar == -1) {
                            encrypted = encrypted + text.charAt(l);
                        } else {
                            if (currentChar - keyChar < 0) {
                                encrypted = encrypted + mask.charAt(currentChar - keyChar + mask.length());
                            } else {
                                encrypted = encrypted + mask.charAt(currentChar - keyChar);
                            }
                        }
                        if (keyPart == key.length() - 1) {
                            keyPart = 0;
                        } else {
                            keyPart++;
                        }
                    }
                    return encrypted;
                }
                return text;
            }

            private static int findChar(char s, String mask) {
                for (int cu = 0; cu < mask.length(); cu++) {
                    if (s == mask.charAt(cu)) {
                        return cu;
                    }
                }
                return -1;
            }
        }

        public static class EncryptionV2 {
            public static String encrypt(String key, String text) {
                int[] keyNode = new int[key.length()];
                int[] node1 = new int[text.length()];
                int[] node2 = new int[text.length()];
                char[] node3 = new char[text.length()];
                boolean up = true;
                int keyPart = 0;
                for (int kp = 0; kp < key.length(); kp++) {
                    keyNode[kp] = key.charAt(kp);
                }
                for (int l = 0; l < text.length(); l++) {
                    node1[l] = findChar(text.charAt(l), DEFAULT);
                }
                for (int l = 0; l < text.length(); l++) {
                    if (keyPart < keyNode.length - 1) {
                        keyPart++;
                    } else {
                        keyPart = 0;
                    }
                    if (up) {
                        node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                    } else {
                        node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                    }
                    up = !up;
                }
                for (int l = 0; l < text.length(); l++) {
                    node3[l] = findInt(node2[l], DEFAULT);
                }
                return String.copyValueOf(node3);
            }

            public static String decrypt(String key, String text) {
                int[] keyNode = new int[key.length()];
                int[] node1 = new int[text.length()];
                int[] node2 = new int[text.length()];
                char[] node3 = new char[text.length()];
                boolean up = true;
                int keyPart = 0;
                for (int kp = 0; kp < key.length(); kp++) {
                    keyNode[kp] = key.charAt(kp);
                }
                for (int l = 0; l < text.length(); l++) {
                    node1[l] = findChar(text.charAt(l), DEFAULT);
                }
                for (int l = 0; l < text.length(); l++) {
                    if (keyPart < keyNode.length - 1) {
                        keyPart++;
                    } else {
                        keyPart = 0;
                    }
                    if (up) {
                        node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                    } else {
                        node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                    }
                    up = !up;
                }
                for (int l = 0; l < text.length(); l++) {
                    node3[l] = findInt(node2[l], DEFAULT);
                }
                return String.copyValueOf(node3);
            }

            public static String encrypt(String key, String text, String map) {
                int[] keyNode = new int[key.length()];
                int[] node1 = new int[text.length()];
                int[] node2 = new int[text.length()];
                char[] node3 = new char[text.length()];
                boolean up = true;
                int keyPart = 0;
                for (int kp = 0; kp < key.length(); kp++) {
                    keyNode[kp] = key.charAt(kp);
                }
                for (int l = 0; l < text.length(); l++) {
                    node1[l] = findChar(text.charAt(l), map);
                }
                for (int l = 0; l < text.length(); l++) {
                    if (keyPart < keyNode.length - 1) {
                        keyPart++;
                    } else {
                        keyPart = 0;
                    }
                    if (up) {
                        node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                    } else {
                        node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                    }
                    up = !up;
                }
                for (int l = 0; l < text.length(); l++) {
                    node3[l] = findInt(node2[l], map);
                }
                return String.copyValueOf(node3);
            }

            public static String decrypt(String key, String text, String map) {
                int[] keyNode = new int[key.length()];
                int[] node1 = new int[text.length()];
                int[] node2 = new int[text.length()];
                char[] node3 = new char[text.length()];
                boolean up = true;
                int keyPart = 0;
                for (int kp = 0; kp < key.length(); kp++) {
                    keyNode[kp] = key.charAt(kp);
                }
                for (int l = 0; l < text.length(); l++) {
                    node1[l] = findChar(text.charAt(l), map);
                }
                for (int l = 0; l < text.length(); l++) {
                    if (keyPart < keyNode.length - 1) {
                        keyPart++;
                    } else {
                        keyPart = 0;
                    }
                    if (up) {
                        node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                    } else {
                        node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                    }
                    up = !up;
                }
                for (int l = 0; l < text.length(); l++) {
                    node3[l] = findInt(node2[l], map);
                }
                return String.copyValueOf(node3);
            }

            private static int findChar(char s, String mask) {
                for (int cu = 0; cu < mask.length(); cu++) {
                    if (s == mask.charAt(cu)) {
                        return cu;
                    }
                }
                return -1;
            }

            public static char findInt(int s, String mask) {
                return mask.charAt(revalueInt(s, mask));
            }

            public static int revalueInt(int s, String mask) {
                while (s > mask.length() - 1) {
                    s = s - (mask.length() - 1);
                }
                while (s < 0) {
                    s = s + (mask.length() - 1);
                }
                return s;
            }

            public static int rIA(int s, int[] array) {
                while (s > array.length - 1) {
                    s = s - (array.length - 1);
                }
                while (s < 0) {
                    s = s + (array.length - 1);
                }
                return s;
            }
        }
    }

    public static class TextAnimator {
        public static final String STOP_ANIMATION = "TEXT_ANIMATION_ACTION_STOP";

        public static Thread animateAppend(final Activity a, final TextView tv, final int millispace) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    final String all = tv.getText().toString();
                    String sofar = "";
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(null);
                        }
                    });
                    for (int c = 0; c < all.length(); c++) {
                        if (!tv.getText().toString().equals(STOP_ANIMATION)) {
                            sofar += all.charAt(c);
                            final String finalSofar = sofar;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(finalSofar);
                                }
                            });
                            try {
                                sleep(millispace);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                    }
                }
            });
        }

        public static Thread animateTypeEffect(final Activity a, final TextView tv, final int millispace) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    final String all = tv.getText().toString();
                    String sofar = "";
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(null);
                        }
                    });
                    for (int c = 0; c < all.length(); c++) {
                        if (!tv.getText().toString().equals(STOP_ANIMATION)) {
                            sofar += all.charAt(c);
                            final String finalSofar = sofar;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(finalSofar);
                                }
                            });
                            int time = new Random().nextInt(millispace);
                            time = time + millispace / 2;
                            try {
                                sleep(time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                    }
                }
            });
        }
    }
}
