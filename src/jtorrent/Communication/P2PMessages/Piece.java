package jtorrent.Communication.P2PMessages;

/**
 * @param content   the byte[] array content of the piece
 * @param pieceHash the Id of the piece according to relevant metadata
 */
public class Piece implements Message {

    private static final long serialVersionUID = 1L;
    private final String messageType = "PIECE";
    private byte[] content = null;
    private String pieceHash = null;

    public Piece(String pieceId, byte[] content) {
        this.pieceHash = pieceId;
        this.content = content;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    public byte[] getContent() {
        return content;
    }

    public String getPieceHash() {
        return pieceHash;
    }

}