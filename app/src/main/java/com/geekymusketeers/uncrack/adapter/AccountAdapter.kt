package com.geekymusketeers.uncrack.adapter


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.uncrack.R
import com.geekymusketeers.uncrack.data.model.Account
import com.geekymusketeers.uncrack.ui.fragments.account.EditFragment

class AccountAdapter(private val context: Context,
                     private val listener: (Account) -> Unit):
    RecyclerView.Adapter<AccountAdapter.ViewHolder>()
{

    private var accountList = emptyList<Account>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAccount = accountList[position]


        holder.itemView.setOnClickListener {

            listener(currentAccount)
        }

        holder.itemView.findViewById<TextView>(R.id.txtCompany).text = currentAccount.company
        holder.itemView.findViewById<TextView>(R.id.txtEmail).text = currentAccount.email

        //  For setting icons of company according to users choice

        when (currentAccount.company.toLowerCase().trim()) {

            "paypal" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.paypal)
            "instagram" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.instagram)
            "facebook" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.facebook)
            "linkedin" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.linkedin)
            "snapchat" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.snapchat)
            "youtube" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.youtube)
            "dropbox" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.dropbox)
            "twitter" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.twitter)
            "google drive" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.drive)
            "netflix" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.netflix_logo)
            "amazon prime" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.amazon_logo)
            "spotify" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.spotify)
            "discord" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.discord)
            "github" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.cl_github)
            "gmail" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.cl_gmail)
            "paytm" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.cl_paytm)
            "quora" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.cl_quora)
            "reddit" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.cl_reddit)
            "others" -> holder.itemView.findViewById<ImageView>(R.id.img_company).setImageResource(R.drawable.general_account)
        }


        holder.itemView.findViewById<ImageView>(R.id.button_edit).setOnClickListener {

            // Passing data to Edit fragments
            val bundle = Bundle()
            bundle.putSerializable("category",currentAccount.category)
            bundle.putSerializable("id",currentAccount.id)
            bundle.putSerializable("company",currentAccount.company)
            bundle.putSerializable("email", currentAccount.email)
            bundle.putSerializable("username", currentAccount.username)
            bundle.putSerializable("password", currentAccount.password)
            bundle.putSerializable("category", currentAccount.category)
            bundle.putSerializable("note",currentAccount.note)
            bundle.putSerializable("dateTime",currentAccount.dateTime)

            val fragment = EditFragment()
            fragment.arguments = bundle
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

    }

    override fun getItemCount(): Int {
        return accountList.size
    }

    fun setData(account: List<Account>){
        accountList = account
        notifyDataSetChanged()
    }
}