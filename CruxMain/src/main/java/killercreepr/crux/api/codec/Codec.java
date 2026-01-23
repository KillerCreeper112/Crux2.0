package killercreepr.crux.api.codec;

import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.*;

public interface Codec<T> {
    T decode(DataNode node) throws DecodeException;
    DataNode encode(T value);

    default DataNode encodeUnchecked(Object value){
        return encode((T) value);
    }

    StringCodec STRING = new StringCodec();
    BoolCodec BOOLEAN = new BoolCodec();
    DoubleCodec DOUBLE = new DoubleCodec();
    IntCodec INTEGER = new IntCodec();
    ByteCodec BYTE = new ByteCodec();
    LongCodec LONG = new LongCodec();
    ShortCodec SHORT = new ShortCodec();

    CruxCodecLists LIST = new CruxCodecLists();

    static <T> ListCodec<T> listCodec(Codec<T> codec) {
        return new ListCodec<>(codec);
    }

    static <E extends Enum<E>> EnumCodec<E> enumCodec(Class<E> type){
        return new EnumCodec<>(type);
    }
}
