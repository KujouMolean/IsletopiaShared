import com.molean.isletopia.shared.utils.LangUtils;
import org.bukkit.block.Biome;

import java.util.Locale;

public class MyTest {
    public static void main(String[] args) {
        for (Biome value : Biome.values()) {
            String s = LangUtils.get(Locale.SIMPLIFIED_CHINESE, "biome." + value.getKey().namespace() + "." + value.getKey().value());
            System.out.println(s);
        }

    }
}
