package com.SJY.O2O_Automatic_Store_System_Demo.entity.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessage is a Querydsl query type for Message
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessage extends EntityPathBase<Message> {

    private static final long serialVersionUID = -2080831858L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessage message = new QMessage("message");

    public final com.SJY.O2O_Automatic_Store_System_Demo.entity.common.QEntityDate _super = new com.SJY.O2O_Automatic_Store_System_Demo.entity.common.QEntityDate(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deletedByReceiver = createBoolean("deletedByReceiver");

    public final BooleanPath deletedBySender = createBoolean("deletedBySender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.SJY.O2O_Automatic_Store_System_Demo.entity.member.QMember receiver;

    public final com.SJY.O2O_Automatic_Store_System_Demo.entity.member.QMember sender;

    public QMessage(String variable) {
        this(Message.class, forVariable(variable), INITS);
    }

    public QMessage(Path<? extends Message> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessage(PathMetadata metadata, PathInits inits) {
        this(Message.class, metadata, inits);
    }

    public QMessage(Class<? extends Message> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new com.SJY.O2O_Automatic_Store_System_Demo.entity.member.QMember(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new com.SJY.O2O_Automatic_Store_System_Demo.entity.member.QMember(forProperty("sender")) : null;
    }

}

