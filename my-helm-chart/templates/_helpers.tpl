{{/*
Expand the name of the chart.
*/}}

{{- define "restful.labels" -}}
apps: restful-by-helm3
check01: chien-deploy
verify: all-check-by-HELM
{{- end }}