package killercreepr.crux.api.codec;

import killercreepr.crux.core.codec.ListCodec;
import net.kyori.adventure.key.Key;
import org.bukkit.util.Vector;

import java.util.UUID;

public class CruxCodecLists {
  public final ListCodec<String> STRING =
    Codec.listCodec(Codec.STRING);

  public final ListCodec<Boolean> BOOLEAN =
    Codec.listCodec(Codec.BOOLEAN);

  public final ListCodec<Double> DOUBLE =
    Codec.listCodec(Codec.DOUBLE);

  public final ListCodec<Integer> INTEGER =
    Codec.listCodec(Codec.INTEGER);

  public final ListCodec<Byte> BYTE =
    Codec.listCodec(Codec.BYTE);

  public final ListCodec<Long> LONG =
    Codec.listCodec(Codec.LONG);

  public final ListCodec<Short> SHORT =
    Codec.listCodec(Codec.SHORT);

  public final ListCodec<Key> KEY =
    Codec.listCodec(Codec.KEY);




  public final ListCodec<Vector> VECTOR =
    Codec.listCodec(CruxCodecs.VECTOR);

  public final ListCodec<UUID> UUID =
    Codec.listCodec(CruxCodecs.UUID);
}
