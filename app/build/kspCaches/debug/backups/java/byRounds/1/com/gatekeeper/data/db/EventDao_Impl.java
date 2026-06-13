package com.gatekeeper.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gatekeeper.data.model.EventContact;
import com.gatekeeper.data.model.EventWithContacts;
import com.gatekeeper.data.model.GateEvent;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EventDao_Impl extends EventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GateEvent> __insertionAdapterOfGateEvent;

  private final EntityInsertionAdapter<EventContact> __insertionAdapterOfEventContact;

  private final EntityDeletionOrUpdateAdapter<GateEvent> __deletionAdapterOfGateEvent;

  private final EntityDeletionOrUpdateAdapter<GateEvent> __updateAdapterOfGateEvent;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEventContacts;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGateEvent = new EntityInsertionAdapter<GateEvent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `events` (`id`,`name`,`startEpochMs`,`endEpochMs`,`isEnabled`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GateEvent entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getStartEpochMs());
        statement.bindLong(4, entity.getEndEpochMs());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__insertionAdapterOfEventContact = new EntityInsertionAdapter<EventContact>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `event_contacts` (`eventId`,`name`,`phoneNumber`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventContact entity) {
        statement.bindLong(1, entity.getEventId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getPhoneNumber());
      }
    };
    this.__deletionAdapterOfGateEvent = new EntityDeletionOrUpdateAdapter<GateEvent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GateEvent entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGateEvent = new EntityDeletionOrUpdateAdapter<GateEvent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `events` SET `id` = ?,`name` = ?,`startEpochMs` = ?,`endEpochMs` = ?,`isEnabled` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GateEvent entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getStartEpochMs());
        statement.bindLong(4, entity.getEndEpochMs());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteEventContacts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM event_contacts WHERE eventId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final GateEvent event, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGateEvent.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEventContacts(final List<EventContact> contacts,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEventContact.insert(contacts);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEvent(final GateEvent event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGateEvent.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEvent(final GateEvent event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGateEvent.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertEventWithContacts(final GateEvent event, final List<EventContact> contacts,
      final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> EventDao_Impl.super.upsertEventWithContacts(event, contacts, __cont), $completion);
  }

  @Override
  public Object deleteEventContacts(final long eventId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEventContacts.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, eventId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteEventContacts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventWithContacts>> getAllEventsWithContacts() {
    final String _sql = "SELECT * FROM events ORDER BY startEpochMs ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"event_contacts",
        "events"}, new Callable<List<EventWithContacts>>() {
      @Override
      @NonNull
      public List<EventWithContacts> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfStartEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochMs");
            final int _cursorIndexOfEndEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochMs");
            final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
            final LongSparseArray<ArrayList<EventContact>> _collectionContacts = new LongSparseArray<ArrayList<EventContact>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionContacts.containsKey(_tmpKey)) {
                _collectionContacts.put(_tmpKey, new ArrayList<EventContact>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipeventContactsAscomGatekeeperDataModelEventContact(_collectionContacts);
            final List<EventWithContacts> _result = new ArrayList<EventWithContacts>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final EventWithContacts _item;
              final GateEvent _tmpEvent;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final long _tmpStartEpochMs;
              _tmpStartEpochMs = _cursor.getLong(_cursorIndexOfStartEpochMs);
              final long _tmpEndEpochMs;
              _tmpEndEpochMs = _cursor.getLong(_cursorIndexOfEndEpochMs);
              final boolean _tmpIsEnabled;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
              _tmpIsEnabled = _tmp != 0;
              _tmpEvent = new GateEvent(_tmpId,_tmpName,_tmpStartEpochMs,_tmpEndEpochMs,_tmpIsEnabled);
              final ArrayList<EventContact> _tmpContactsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpContactsCollection = _collectionContacts.get(_tmpKey_1);
              _item = new EventWithContacts(_tmpEvent,_tmpContactsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getActiveEventsWithContacts(final long now,
      final Continuation<? super List<EventWithContacts>> $completion) {
    final String _sql = "SELECT * FROM events WHERE isEnabled = 1 AND startEpochMs <= ? AND endEpochMs >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, now);
    _argIndex = 2;
    _statement.bindLong(_argIndex, now);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<EventWithContacts>>() {
      @Override
      @NonNull
      public List<EventWithContacts> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfStartEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochMs");
            final int _cursorIndexOfEndEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochMs");
            final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
            final LongSparseArray<ArrayList<EventContact>> _collectionContacts = new LongSparseArray<ArrayList<EventContact>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionContacts.containsKey(_tmpKey)) {
                _collectionContacts.put(_tmpKey, new ArrayList<EventContact>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipeventContactsAscomGatekeeperDataModelEventContact(_collectionContacts);
            final List<EventWithContacts> _result = new ArrayList<EventWithContacts>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final EventWithContacts _item;
              final GateEvent _tmpEvent;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final long _tmpStartEpochMs;
              _tmpStartEpochMs = _cursor.getLong(_cursorIndexOfStartEpochMs);
              final long _tmpEndEpochMs;
              _tmpEndEpochMs = _cursor.getLong(_cursorIndexOfEndEpochMs);
              final boolean _tmpIsEnabled;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
              _tmpIsEnabled = _tmp != 0;
              _tmpEvent = new GateEvent(_tmpId,_tmpName,_tmpStartEpochMs,_tmpEndEpochMs,_tmpIsEnabled);
              final ArrayList<EventContact> _tmpContactsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpContactsCollection = _collectionContacts.get(_tmpKey_1);
              _item = new EventWithContacts(_tmpEvent,_tmpContactsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEventWithContacts(final long id,
      final Continuation<? super EventWithContacts> $completion) {
    final String _sql = "SELECT * FROM events WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<EventWithContacts>() {
      @Override
      @Nullable
      public EventWithContacts call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfStartEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochMs");
            final int _cursorIndexOfEndEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochMs");
            final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
            final LongSparseArray<ArrayList<EventContact>> _collectionContacts = new LongSparseArray<ArrayList<EventContact>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionContacts.containsKey(_tmpKey)) {
                _collectionContacts.put(_tmpKey, new ArrayList<EventContact>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipeventContactsAscomGatekeeperDataModelEventContact(_collectionContacts);
            final EventWithContacts _result;
            if (_cursor.moveToFirst()) {
              final GateEvent _tmpEvent;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final long _tmpStartEpochMs;
              _tmpStartEpochMs = _cursor.getLong(_cursorIndexOfStartEpochMs);
              final long _tmpEndEpochMs;
              _tmpEndEpochMs = _cursor.getLong(_cursorIndexOfEndEpochMs);
              final boolean _tmpIsEnabled;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
              _tmpIsEnabled = _tmp != 0;
              _tmpEvent = new GateEvent(_tmpId,_tmpName,_tmpStartEpochMs,_tmpEndEpochMs,_tmpIsEnabled);
              final ArrayList<EventContact> _tmpContactsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpContactsCollection = _collectionContacts.get(_tmpKey_1);
              _result = new EventWithContacts(_tmpEvent,_tmpContactsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipeventContactsAscomGatekeeperDataModelEventContact(
      @NonNull final LongSparseArray<ArrayList<EventContact>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipeventContactsAscomGatekeeperDataModelEventContact(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `eventId`,`name`,`phoneNumber` FROM `event_contacts` WHERE `eventId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "eventId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfEventId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfPhoneNumber = 2;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<EventContact> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final EventContact _item_1;
          final long _tmpEventId;
          _tmpEventId = _cursor.getLong(_cursorIndexOfEventId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final String _tmpPhoneNumber;
          _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
          _item_1 = new EventContact(_tmpEventId,_tmpName,_tmpPhoneNumber);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
