package tv.engine.classes

import grails.util.Holders
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject
import tv.api.orm.User
import tv.engine.annotations.Table
import tv.engine.annotations.Column as Col
import tv.engine.annotations.Context
import tv.engine.annotations.HABTM
import tv.engine.exceptions.InstanceNotLoadedException

import java.lang.reflect.Field


class ORM extends EngineClient {

    String identifier

    def grailsApplication = Holders.grailsApplication.mainContext.getBean('grailsApplication')

    String context
    String table
    List map

    ORM() {
        // ensure context existence
        if (!super.exists(String.format("/%s", this.getContext()))){
            super.post(String.format("/%s", this.getContext()), [])
        }
        if (!super.exists(String.format("/%s/%s", this.getContext(), this.getTableName()))){
            println("Trying to create Table: " + this.getTableName())
            super.post(String.format("/%s/%s/map", this.getContext(), this.getTableName()), this.createMap())
            println("Created Table: " + this.getTableName())
        }
    }

    def fetchRelations(JSONObject ds){
        def habtm = this.getHABTMAnnotationData()

        println(habtm)
        /** @TODO
        if (habtm != ""){
            def identifierKey = grailsApplication.config.getProperty("tv.engine.identifier")
            def list = new JSONArray()
            def query =  new JSONObject().put(habtm.column, this.identifier)
            JSONArray res = this.post(String.format("/%s/%s/query", habtm.context, habtm.table), query)
            res.each {  r ->
                try {
                    list.add(r.getJSONObject(habtm.column))
                } catch(Exception e){

                }
            }
            ds.put(habtm.column, list)
        }*/vv

        return ds
    }

    def getIdentifier(){
        return this.identifier
    }
    def setIdentifier(String identifier){
        this.identifier = identifier
    }

    def toData(Boolean fetchRelations = true){
        def dataSet = new JSONObject()
        def instance = grailsApplication.getClassLoader().loadClass( this.getClass().name)?.newInstance()
        def identifierKey = grailsApplication.config.getProperty("tv.engine.identifier")
        instance.getColumns().each { column ->
            if (column.name() != identifierKey) {
                def method = String.format("get%s", column.name().capitalize())
                dataSet.put(column.name(), this."${method}"())
            }
        }
        dataSet.put(identifierKey, this.identifier)
        if (fetchRelations){
            dataSet = instance.fetchRelations(dataSet)
        }

        return dataSet
    }


    def createMap(){
        def map = new HashMap()
        this.getColumns().each { column ->
            map.put(column.name(), [
                    type: column.type(),
                    maxLength: column.maxLength(),
                    nullable: column.nullable(),
                    context: column.context(),
                    table: column.table()
            ])
        }

        return map
    }

    def load(String identifier){
        JSONObject res = this.find(identifier)
        if (res == null){
            return null
        }

        def instance = grailsApplication.getClassLoader().loadClass( this.getClass().name)?.newInstance()
        def identifierKey = grailsApplication.config.getProperty("tv.engine.identifier")
        res.keys().forEachRemaining { key ->
            if (key != identifierKey) {
                instance.setProperty(key, res.get(key))
            }
        }


        instance.setIdentifier(res.get(identifierKey))

        return instance
    }

    def update(updates){
        if (this.identifier == null){
            throw new InstanceNotLoadedException()
        }

        def data = new JSONObject()
        this.getColumns().each { column ->
            //def method = String.format("get%s", column.name().capitalize())
            if (updates.containsKey(column.name())){
                data.put(column.name(), updates.get(column.name()))
            }
        }

        return this.put(String.format("/%s/%s/%s", this.getContext(), this.getTableName(), this.identifier), data)
    }

    def update(JSONObject updates){
        if (this.identifier == null){
            throw new InstanceNotLoadedException()
        }


        // Extra step to filter out erroneous data
        def data = new JSONObject()
        this.getColumns().each { column ->
            //def method = String.format("get%s", column.name().capitalize())
            if (updates.has(column.name())){
                data.put(column.name(), updates.get(column.name()))
            }
        }

        return this.put(String.format("/%s/%s/%s", this.getContext(), this.getTableName(), this.identifier), data)
    }

    def remove(){
        if (this.identifier == null){
            throw new InstanceNotLoadedException()
        }

        return this.delete(String.format("/%s/%s/%s", this.getContext(), this.getTableName(), this.identifier))
    }


    def save(){
        def o = new JSONObject()
        def self = this
        this.getColumns().each { column ->
            o.put(column.name(), self.getProperty(column.name()))
        }
        if (this.identifier != null){
            return this.update(o)
        }
        return this.post(String.format("/%s/%s", this.getContext(), this.getTableName()), o)
    }

    def selectAll(){
        return this.get(String.format("/%s/%s/all", this.getContext(), this.getTableName()))
    }

    def findAll(){
        return this.selectAll()
    }

    def find(String identifier){
        def res = super.get( String.format("/%s/%s/%s", this.getContext(), this.getTableName(), identifier))
        if (res.size() == 0){
            return null
        }
        return res
    }

    def findBy(params = []){
        return this.post(String.format("/%s/%s/query", this.getContext(), this.getTableName()), params)
    }

    def getHABTMAnnotationData(){
        return [
                entity: this.getClass().getAnnotation(HABTM).entity(),
                table: this.getClass().getAnnotation(HABTM).table(),
                column: this.getClass().getAnnotation(HABTM).column(),
                name: this.getClass().getAnnotation(HABTM).name(),
                context: this.getClass().getAnnotation(HABTM).context()
        ]
    }

    def getTableName(){
        return this.getClass().getAnnotation(Table).value()
    }

    def getContext(){
        return this.getClass().getAnnotation(Context).value()
    }

    def getColumns() {
        def list = []
        for (Field f: this.class.getDeclaredFields()) {
            Col annotation = f.getAnnotation(Col.class)
            if (annotation != null) {
                list.add(annotation)
            }
        }
        return list
    }
}
