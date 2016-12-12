package org.spacebison.recyclerviewlistadapter;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by cmb on 12.12.16.
 */

public abstract class RecyclerViewListAdapter<T, V extends BindableViewHolder<T, V>> extends RecyclerView.Adapter<V> implements List<T> {
    private final LinkedList<T> mData = new LinkedList<>();
    private OnItemClickListener<T, V> mOnItemClickListener;
    private OnItemLongClickListener<T, V> mOnItemLongClickListener;

    public abstract V onCreateBindableViewHolder(ViewGroup parent, int viewType);

    @Override
    public final V onCreateViewHolder(ViewGroup parent, int viewType) {
        final V holder = onCreateBindableViewHolder(parent, viewType);

        holder.setOnClickListener(new BindableViewHolder.OnClickListener<T, V>() {
            @Override
            public void onViewHolderClick(V holder) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(RecyclerViewListAdapter.this, holder);
                }
            }

            @Override
            public boolean onViewHolderLongClick(V holder) {
                if (mOnItemLongClickListener != null) {
                    return mOnItemLongClickListener.onItemLongClick(RecyclerViewListAdapter.this, holder);
                } else {
                    return false;
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return size();
    }

    public void setOnItemClickListener(OnItemClickListener<T, V> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T, V> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    @Override
    public int size() {
        return mData.size();
    }

    @Override
    public boolean isEmpty() {
        return mData.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mData.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mData.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] t1s) {
        return mData.toArray(t1s);
    }

    @UiThread
    @Override
    public boolean add(T t) {
        mData.add(t);
        notifyItemInserted(mData.size() - 1);
        return true;
    }

    @UiThread
    @Override
    public boolean remove(Object o) {
        final int index = mData.indexOf(o);

        if (index >= 0) {
            mData.remove(index);
            notifyItemRemoved(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return mData.containsAll(collection);
    }

    @UiThread
    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        final int oldSize = mData.size();
        mData.addAll(collection);
        notifyItemRangeInserted(oldSize, collection.size());
        return true;
    }

    @UiThread
    @Override
    public boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        mData.addAll(i, collection);
        notifyItemRangeInserted(i, collection.size());
        return true;
    }

    @UiThread
    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        int oldSize = mData.size();
        final Iterator<T> iterator = mData.iterator();
        int index = 0;

        while (iterator.hasNext()) {
            if (collection.contains(iterator.next())) {
                iterator.remove();
                notifyItemRemoved(index);
            } else {
                ++index;
            }
        }

        return mData.size() < oldSize;
    }

    @UiThread
    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        int oldSize = mData.size();
        final Iterator<T> iterator = mData.iterator();
        int index = 0;

        while (iterator.hasNext()) {
            if (!collection.contains(iterator.next())) {
                iterator.remove();
                notifyItemRemoved(index);
            } else {
                ++index;
            }
        }

        return mData.size() < oldSize;
    }

    @UiThread
    @Override
    public void clear() {
        int oldSize = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    @Override
    public T get(int i) {
        return mData.get(i);
    }

    /**
     * Returns the element represented by the passed view holder.
     *
     * @param viewHolder view holder representing the element
     * @return T the element represented by the view holder
     */
    public T get(V viewHolder) {
        return mData.get(viewHolder.getAdapterPosition());
    }

    @UiThread
    @Override
    public T set(int i, T t) {
        final T oldValue = mData.set(i, t);
        notifyItemChanged(i);
        return oldValue;
    }

    @UiThread
    @Override
    public void add(int i, T t) {
        mData.add(i, t);
        notifyItemInserted(i);
    }

    @UiThread
    @Override
    public T remove(int i) {
        final T removed = mData.remove();
        notifyItemRemoved(i);
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return mData.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mData.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i1) {
        return mData.subList(i, i1);
    }
}