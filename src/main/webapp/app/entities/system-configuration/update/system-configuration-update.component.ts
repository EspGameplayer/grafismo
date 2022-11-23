import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISystemConfiguration, SystemConfiguration } from '../system-configuration.model';
import { SystemConfigurationService } from '../service/system-configuration.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';
import { ISponsor } from 'app/entities/sponsor/sponsor.model';
import { SponsorService } from 'app/entities/sponsor/service/sponsor.service';

@Component({
  selector: 'jhi-system-configuration-update',
  templateUrl: './system-configuration-update.component.html',
})
export class SystemConfigurationUpdateComponent implements OnInit {
  isSaving = false;

  currentSeasonsCollection: ISeason[] = [];
  defaultSponsorLogosCollection: ISponsor[] = [];
  defaultMotmSponsorLogosCollection: ISponsor[] = [];

  editForm = this.fb.group({
    id: [],
    currentPeriodStartMoment: [],
    currentSeason: [],
    defaultSponsorLogo: [],
    defaultMotmSponsorLogo: [],
  });

  constructor(
    protected systemConfigurationService: SystemConfigurationService,
    protected seasonService: SeasonService,
    protected sponsorService: SponsorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemConfiguration }) => {
      if (systemConfiguration.id === undefined) {
        const today = dayjs().startOf('day');
        systemConfiguration.currentPeriodStartMoment = today;
      }

      this.updateForm(systemConfiguration);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemConfiguration = this.createFromForm();
    if (systemConfiguration.id !== undefined) {
      this.subscribeToSaveResponse(this.systemConfigurationService.update(systemConfiguration));
    } else {
      this.subscribeToSaveResponse(this.systemConfigurationService.create(systemConfiguration));
    }
  }

  trackSeasonById(_index: number, item: ISeason): number {
    return item.id!;
  }

  trackSponsorById(_index: number, item: ISponsor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemConfiguration>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(systemConfiguration: ISystemConfiguration): void {
    this.editForm.patchValue({
      id: systemConfiguration.id,
      currentPeriodStartMoment: systemConfiguration.currentPeriodStartMoment
        ? systemConfiguration.currentPeriodStartMoment.format(DATE_TIME_FORMAT)
        : null,
      currentSeason: systemConfiguration.currentSeason,
      defaultSponsorLogo: systemConfiguration.defaultSponsorLogo,
      defaultMotmSponsorLogo: systemConfiguration.defaultMotmSponsorLogo,
    });

    this.currentSeasonsCollection = this.seasonService.addSeasonToCollectionIfMissing(
      this.currentSeasonsCollection,
      systemConfiguration.currentSeason
    );
    this.defaultSponsorLogosCollection = this.sponsorService.addSponsorToCollectionIfMissing(
      this.defaultSponsorLogosCollection,
      systemConfiguration.defaultSponsorLogo
    );
    this.defaultMotmSponsorLogosCollection = this.sponsorService.addSponsorToCollectionIfMissing(
      this.defaultMotmSponsorLogosCollection,
      systemConfiguration.defaultMotmSponsorLogo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.seasonService
      .query({ filter: 'systemconfiguration-is-null' })
      .pipe(map((res: HttpResponse<ISeason[]>) => res.body ?? []))
      .pipe(
        map((seasons: ISeason[]) => this.seasonService.addSeasonToCollectionIfMissing(seasons, this.editForm.get('currentSeason')!.value))
      )
      .subscribe((seasons: ISeason[]) => (this.currentSeasonsCollection = seasons));

    this.sponsorService
      .query({ filter: 'systemconfiguration-is-null' })
      .pipe(map((res: HttpResponse<ISponsor[]>) => res.body ?? []))
      .pipe(
        map((sponsors: ISponsor[]) =>
          this.sponsorService.addSponsorToCollectionIfMissing(sponsors, this.editForm.get('defaultSponsorLogo')!.value)
        )
      )
      .subscribe((sponsors: ISponsor[]) => (this.defaultSponsorLogosCollection = sponsors));

    this.sponsorService
      .query({ filter: 'systemconfiguration-is-null' })
      .pipe(map((res: HttpResponse<ISponsor[]>) => res.body ?? []))
      .pipe(
        map((sponsors: ISponsor[]) =>
          this.sponsorService.addSponsorToCollectionIfMissing(sponsors, this.editForm.get('defaultMotmSponsorLogo')!.value)
        )
      )
      .subscribe((sponsors: ISponsor[]) => (this.defaultMotmSponsorLogosCollection = sponsors));
  }

  protected createFromForm(): ISystemConfiguration {
    return {
      ...new SystemConfiguration(),
      id: this.editForm.get(['id'])!.value,
      currentPeriodStartMoment: this.editForm.get(['currentPeriodStartMoment'])!.value
        ? dayjs(this.editForm.get(['currentPeriodStartMoment'])!.value, DATE_TIME_FORMAT)
        : undefined,
      currentSeason: this.editForm.get(['currentSeason'])!.value,
      defaultSponsorLogo: this.editForm.get(['defaultSponsorLogo'])!.value,
      defaultMotmSponsorLogo: this.editForm.get(['defaultMotmSponsorLogo'])!.value,
    };
  }
}
