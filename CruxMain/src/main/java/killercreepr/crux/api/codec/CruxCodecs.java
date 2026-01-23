package killercreepr.crux.api.codec;

import killercreepr.crux.api.codec.builder.CodecBuilder;
import killercreepr.crux.core.codec.UUIDCodec;
import org.bukkit.util.Vector;

import java.util.UUID;

public class CruxCodecs {
  public static final Codec<Vector> VECTOR = CodecBuilder.builder(Vector::new)
    .field("x", Codec.DOUBLE, Vector::getX, Vector::setX)
    .field("y", Codec.DOUBLE, Vector::getY, Vector::setY)
    .field("z", Codec.DOUBLE, Vector::getZ, Vector::setZ)
    .build();

  public static final Codec<UUID> UUID = new UUIDCodec();


  public static CruxCodecLists LIST = Codec.LIST;
}
